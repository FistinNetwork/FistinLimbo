package fr.fistin.limbo.network.protocol.login;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutEncryptionRequest;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutSuccess;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.encryption.EncryptionUtil;
import fr.fistin.limbo.network.protocol.login.auth.exception.AuthenticationUnavailableException;
import fr.fistin.limbo.network.protocol.login.auth.profile.GameProfile;
import fr.fistin.limbo.network.protocol.login.auth.yggdrasil.YggdrasilAuthenticationService;
import fr.fistin.limbo.network.protocol.login.auth.yggdrasil.YggdrasilMinecraftSessionService;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.util.UUIDUtil;
import fr.fistin.limbo.world.Chunk;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 15:08
 */
public class LoginManager {

    private final YggdrasilMinecraftSessionService minecraftSessionService;

    private final Limbo limbo;

    public LoginManager(Limbo limbo) {
        this.limbo = limbo;
        this.minecraftSessionService = new YggdrasilAuthenticationService(UUID.randomUUID().toString()).createMinecraftSessionService();
    }

    public void loginStart(PlayerConnection playerConnection, String username) {
        final Limbo limbo = playerConnection.getLimbo();

        if (limbo.getConfiguration().isOnlineMode() && !playerConnection.isLocal()) {
            playerConnection.setProfile(new GameProfile(null, username));

            playerConnection.sendPacket(new PacketLoginOutEncryptionRequest("", limbo.getKeyPair().getPublic(), limbo.getVerifyToken()));
        } else {
            playerConnection.setProfile(new GameProfile(UUIDUtil.getOffline(username), username));

            this.loginSuccess(playerConnection);
        }
    }

    public void auth(PlayerConnection playerConnection, SecretKey secretKey) {
        new Thread(() -> {
            GameProfile profile = playerConnection.getProfile();

            try {
                final String serverId = (new BigInteger(EncryptionUtil.digestKey("", limbo.getKeyPair().getPublic(), secretKey)).toString(16));

                playerConnection.setProfile(this.minecraftSessionService.hasJoinedServer(playerConnection.getProfile(), serverId));

                profile = playerConnection.getProfile();

                if (profile != null) {
                    playerConnection.enableEncryption(secretKey);

                    System.out.println("UUID of player " + profile.getName() + " is " + profile.getId());

                    this.loginSuccess(playerConnection);
                } else {
                    playerConnection.disconnect("Failed to verify username!");

                    System.err.println("Username '" + profile.getName() + "' tried to join with an invalid session!");
                }
            } catch (AuthenticationUnavailableException e) {
                playerConnection.disconnect("Authentication servers are down. Please try again later, sorry!");

                System.err.println("Couldn't verify username because servers are unavailable!");
            } catch (Exception e) {
                playerConnection.disconnect("Failed to verify username!");

                Limbo.getLogger().log(Level.WARNING, "Exception verifying " + profile.getName() + ".", e);
            }
        }).start();
    }

    private void hasJoinedServer(GameProfile profile, String serverId) {
        try {
            final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + profile.getName() + "&serverId=" + serverId);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            final int responseCode = connection.getResponseCode();

            System.out.println("RC: " + responseCode);

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder response = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginSuccess(PlayerConnection playerConnection) {
        final GameProfile profile = playerConnection.getProfile();

        playerConnection.sendPacket(new PacketLoginOutSuccess(profile.getName(), profile.getId()));
        playerConnection.setState(ProtocolState.PLAY);

        this.sendJoinGamePacket(playerConnection);
        this.sendChunksAndPlayerPosition(playerConnection);

        playerConnection.startKeepAliveFuture();
    }

    private void sendJoinGamePacket(PlayerConnection playerConnection) {
        final LimboConfiguration configuration = this.limbo.getConfiguration();
        final byte gamemode = configuration.getGameMode();
        final byte dimension = configuration.getDimension();
        final boolean reducedDebugInfo = configuration.isReducedDebugInfo();

        playerConnection.getProtocol().sendJoinGame(playerConnection, 1, gamemode, dimension, (byte) 0, (byte) 1, "default", reducedDebugInfo);
    }

    private void sendChunksAndPlayerPosition(PlayerConnection playerConnection) {
        new Thread(() -> {
            try {
                Thread.sleep(700);

                for (Chunk[] tab : this.limbo.getWorld().getChunks()) {
                    for (Chunk chunk : tab) {
                        if (chunk != null) {
                            playerConnection.getProtocol().sendChunk(playerConnection, chunk);
                            //chunk.sendTileEntities(playerConnection);
                        }
                    }
                }

                Thread.sleep(300);

                playerConnection.getProtocol().sendPosition(playerConnection, this.limbo.getConfiguration().getSpawnX(), this.limbo.getConfiguration().getSpawnY(), this.limbo.getConfiguration().getSpawnZ(), this.limbo.getConfiguration().getSpawnYaw(), this.limbo.getConfiguration().getSpawnPitch());
            }
            catch (InterruptedException ex) {
                Limbo.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }).start();
    }

}
