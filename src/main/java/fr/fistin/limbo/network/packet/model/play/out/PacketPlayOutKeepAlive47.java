package fr.fistin.limbo.network.packet.model.play.out;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 18:49
 */
public class PacketPlayOutKeepAlive47 extends PacketOutput {

    private final int id;

    public PacketPlayOutKeepAlive47(int id) {
        this.id = id;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        PacketSerializer.writeVarInt(byteBuf, this.id);
    }

}
