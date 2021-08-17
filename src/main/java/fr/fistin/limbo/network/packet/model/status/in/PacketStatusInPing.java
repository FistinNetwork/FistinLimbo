package fr.fistin.limbo.network.packet.model.status.in;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.model.status.out.PacketStatusOutPong;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.packet.PacketInput;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 12:04
 */
public class PacketStatusInPing extends PacketInput {

    private long id;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.id = byteBuf.readLong();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        playerConnection.sendPacket(new PacketStatusOutPong(this.id));
        playerConnection.disconnect(null);
    }

}
