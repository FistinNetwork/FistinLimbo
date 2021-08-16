package fr.fistin.limbo.network.pipeline;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 15:12
 */
public class PacketEncoder extends MessageToByteEncoder<PacketOutput> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PacketOutput packet, ByteBuf out) throws IOException {
        PacketSerializer.writeVarInt(out, packet.getId());

        packet.write(out);
    }

}
