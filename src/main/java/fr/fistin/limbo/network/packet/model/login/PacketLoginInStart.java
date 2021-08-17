package fr.fistin.limbo.network.packet.model.login;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.player.profile.GameProfile;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.util.UUIDUtil;
import fr.fistin.limbo.world.Chunk;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 16:18
 */
public class PacketLoginInStart extends PacketInput {

    private String username;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.username = PacketSerializer.readString(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final GameProfile profile = playerConnection.getProfile();

        profile.setUsername(this.username);

        if (profile.getUuid() == null) {
            profile.setUuid(UUIDUtil.getOffline(this.username));
        }

        playerConnection.sendPacket(new PacketLoginOutSuccess(profile.getUsername(), profile.getUuid()));
        playerConnection.setState(ProtocolState.PLAY);

        this.sendJoinGamePacket(playerConnection);

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

        playerConnection.startKeepAliveFuture();
    }

    private void sendJoinGamePacket(PlayerConnection playerConnection) {
        final LimboConfiguration configuration = playerConnection.getLimbo().getConfiguration();
        final byte gamemode = configuration.getGameMode();
        final byte dimension = configuration.getDimension();
        final boolean reducedDebugInfo = configuration.isReducedDebugInfo();

        playerConnection.getProtocol().sendJoinGame(playerConnection, 1, gamemode, dimension, (byte) 0, (byte) 1, "default", reducedDebugInfo);
    }

}
