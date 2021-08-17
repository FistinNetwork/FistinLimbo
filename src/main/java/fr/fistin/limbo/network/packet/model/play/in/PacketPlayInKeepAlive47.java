package fr.fistin.limbo.network.packet.model.play.in;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 18:31
 */
public class PacketPlayInKeepAlive47 extends PacketInput {

    private int id;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.id = PacketSerializer.readVarInt(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        if (this.id == playerConnection.getKeepAliveId()) {
            playerConnection.setLastKeepAlive(System.currentTimeMillis());
        }
    }

}
