package fr.fistin.limbo.network.packet.model.status.out;

import fr.fistin.limbo.network.packet.PacketOutput;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 12:04
 */
public class PacketStatusOutPong extends PacketOutput {

    private final long id;

    public PacketStatusOutPong(long id) {
        this.id = id;
    }

    @Override
    public void write(ByteBuf buf) throws IOException {
        buf.writeLong(this.id);
    }

}
