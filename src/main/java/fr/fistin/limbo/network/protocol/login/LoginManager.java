package fr.fistin.limbo.network.protocol.login;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutEncryptionRequest;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutSuccess;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.encryption.EncryptionUtil;
import fr.fistin.limbo.network.protocol.login.auth.exception.AuthenticationUnavailableException;
import fr.fistin.limbo.player.profile.GameProfile;
import fr.fistin.limbo.network.protocol.login.auth.yggdrasil.YggdrasilAuthenticationService;
import fr.fistin.limbo.network.protocol.login.auth.yggdrasil.YggdrasilMinecraftSessionService;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.util.UUIDUtil;
import fr.fistin.limbo.world.Chunk;

import javax.crypto.SecretKey;
import java.math.BigInteger;
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

    public void loginSuccess(PlayerConnection playerConnection) {
        final NetworkManager networkManager = this.limbo.getNetworkManager();
        final GameProfile profile = playerConnection.getProfile();
        final String message = profile.getName() + " joined the game";

        playerConnection.sendPacket(new PacketLoginOutSuccess(profile.getName(), profile.getId()));
        playerConnection.setState(ProtocolState.PLAY);

        networkManager.setPlayers(networkManager.getPlayers() + 1);

        System.out.println(message);

        networkManager.sendMessageToAllPlayers(message);

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
