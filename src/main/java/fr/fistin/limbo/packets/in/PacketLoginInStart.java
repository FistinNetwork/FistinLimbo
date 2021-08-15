package fr.fistin.limbo.packets.in;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.NetworkManager;
import fr.fistin.limbo.packets.PacketSerializer;
import fr.fistin.limbo.PlayerConnection;
import fr.fistin.limbo.packets.Status;
import fr.fistin.limbo.packets.out.PacketLoginOutSuccess;
import fr.fistin.limbo.world.Chunk;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketLoginInStart implements PacketIn
{
    private String userName;

    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
        this.userName = packetSerializer.readString();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
        playerConnection.setUserName(this.userName);
        networkManager.sendPacket(playerConnection, new PacketLoginOutSuccess(playerConnection.getUuid(), this.userName));
        playerConnection.setStatus(Status.PLAY);
        playerConnection.getProtocol().sendJoinGame(playerConnection, 1, networkManager.getLimboConfiguration().getGameMode(), networkManager.getLimboConfiguration().getDimension(), (byte)0, (byte)1, "default", networkManager.getLimboConfiguration().isReducedDebugInfo());

        new Thread(() -> {
            try {
                Thread.sleep(700);

                for (Chunk[] tab : networkManager.getWorld().getChunks()) {
                    for (Chunk chunk : tab) {
                        if (chunk != null) {
                            playerConnection.getProtocol().sendChunk(playerConnection, chunk);
                            chunk.sendTileEntities(playerConnection);
                        }
                    }
                }

                Thread.sleep(300);

                playerConnection.getProtocol().sendPosition(playerConnection, networkManager.getLimboConfiguration().getSpawnX(), networkManager.getLimboConfiguration().getSpawnY(), networkManager.getLimboConfiguration().getSpawnZ(), networkManager.getLimboConfiguration().getSpawnYaw(), networkManager.getLimboConfiguration().getSpawnPitch());
            }
            catch (InterruptedException ex) {
                Limbo.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }).start();
    }
}
