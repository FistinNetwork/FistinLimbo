package fr.fistin.limbo.network.pipeline;

import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 18:12
 */
public class VarInt21Encoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        final int bodyLen = msg.readableBytes();
        final int headerLen = PacketSerializer.getVarIntSize(bodyLen);

        out.ensureWritable(headerLen + bodyLen);

        PacketSerializer.writeVarInt(out, bodyLen);

        out.writeBytes(msg);
    }


}
