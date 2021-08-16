package fr.fistin.limbo.network.packet.status;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 12:04
 */
public class PacketStatusInPing extends PacketInput {

    private long id;

    @Override
    public void read(PacketSerializer packetSerializer) throws IOException {
        this.id = packetSerializer.readLong();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        playerConnection.sendPacket(new PacketStatusOutPong(this.id));
        playerConnection.disconnect(null);
    }

}
