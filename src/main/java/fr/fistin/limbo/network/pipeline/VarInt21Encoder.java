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
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        final PacketSerializer serializer = new PacketSerializer(msg);

        final int bodyLen = msg.readableBytes();
        final int headerLen = this.getVarIntSize(bodyLen);

        out.ensureWritable(headerLen + bodyLen);

        serializer.writeVarInt(bodyLen);

        out.writeBytes(msg);
    }

    private int getVarIntSize(int paramInt) {
        if ((paramInt & 0xFFFFFF80) == 0)
            return 1;

        if ((paramInt & 0xFFFFC000) == 0)
            return 2;

        if ((paramInt & 0xFFE00000) == 0)
            return 3;

        if ((paramInt & 0xF0000000) == 0)
            return 4;

        return 5;
    }

}
