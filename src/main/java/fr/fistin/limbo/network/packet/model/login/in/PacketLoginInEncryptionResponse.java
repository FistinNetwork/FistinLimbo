package fr.fistin.limbo.network.packet.model.login.in;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutSuccess;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.protocol.encryption.EncryptionUtil;
import fr.fistin.limbo.player.profile.GameProfile;
import fr.fistin.limbo.world.Chunk;
import io.netty.buffer.ByteBuf;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 08:52
 */
public class PacketLoginInEncryptionResponse extends PacketInput {

    private byte[] sharedSecretEncrypted;
    private byte[] verifyTokenEncrypted;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.sharedSecretEncrypted = PacketSerializer.readByteArray(byteBuf);
        this.verifyTokenEncrypted = PacketSerializer.readByteArray(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final Limbo limbo = playerConnection.getLimbo();
        final PrivateKey privateKey = limbo.getKeyPair().getPrivate();

        if (Arrays.equals(limbo.getVerifyToken(), this.getVerifyToken(privateKey))) {
            final SecretKey secretKey = this.getSecretKey(privateKey);
            final GameProfile profile = playerConnection.getProfile();

            playerConnection.enableEncryption(secretKey);

            playerConnection.sendPacket(new PacketLoginOutSuccess(profile.getUsername(), profile.getUuid()));
            playerConnection.setState(ProtocolState.PLAY);

            this.sendJoinGamePacket(playerConnection);
            this.sendChunksAndPlayerPosition(playerConnection);

            playerConnection.startKeepAliveFuture();
        } else {
            throw new IllegalStateException("Invalid verify token!");
        }
    }

    private void sendJoinGamePacket(PlayerConnection playerConnection) {
        final LimboConfiguration configuration = playerConnection.getLimbo().getConfiguration();
        final byte gamemode = configuration.getGameMode();
        final byte dimension = configuration.getDimension();
        final boolean reducedDebugInfo = configuration.isReducedDebugInfo();

        playerConnection.getProtocol().sendJoinGame(playerConnection, 1, gamemode, dimension, (byte) 0, (byte) 1, "default", reducedDebugInfo);
    }

    private void sendChunksAndPlayerPosition(PlayerConnection playerConnection) {
        new Thread(() -> {
            try {
                final Limbo limbo = playerConnection.getLimbo();

                Thread.sleep(700);

                for (Chunk[] tab : limbo.getWorld().getChunks()) {
                    for (Chunk chunk : tab) {
                        if (chunk != null) {
                            playerConnection.getProtocol().sendChunk(playerConnection, chunk);
                            //chunk.sendTileEntities(playerConnection);
                        }
                    }
                }

                Thread.sleep(300);

                playerConnection.getProtocol().sendPosition(playerConnection, limbo.getConfiguration().getSpawnX(), limbo.getConfiguration().getSpawnY(), limbo.getConfiguration().getSpawnZ(), limbo.getConfiguration().getSpawnYaw(), limbo.getConfiguration().getSpawnPitch());
            }
            catch (InterruptedException ex) {
                Limbo.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }).start();
    }

    private SecretKey getSecretKey(PrivateKey key) {
        return EncryptionUtil.decryptSharedKey(key, this.sharedSecretEncrypted);
    }

    private byte[] getVerifyToken(PrivateKey key) {
        return key == null ? this.verifyTokenEncrypted : EncryptionUtil.decryptData(key, this.verifyTokenEncrypted);
    }

}
