package fr.fistin.limbo.network.packet.model.play.out;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.world.Chunk;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 19:57
 */
public class PacketPlayOutChunkData47 extends PacketOutput {

    private final Chunk chunk;

    public PacketPlayOutChunkData47(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        byteBuf.writeInt(this.chunk.getX());
        byteBuf.writeInt(this.chunk.getZ());
        byteBuf.writeBoolean(true);
        byteBuf.writeShort((short) 65535);
        PacketSerializer.writeByteArray(byteBuf, this.chunk.getMap());
    }

}
