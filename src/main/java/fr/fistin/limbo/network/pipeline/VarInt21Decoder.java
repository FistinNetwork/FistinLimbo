package fr.fistin.limbo.network.pipeline;

import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 16:31
 */
public class VarInt21Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();

        final byte[] buf = new byte[3];

        for (int i = 0; i < buf.length; i++) {
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }

            buf[i] = in.readByte();
            if (buf[i] >= 0) {
                final int length = PacketSerializer.readVarInt(Unpooled.wrappedBuffer(buf));

                if (in.readableBytes() < length) {
                    in.resetReaderIndex();
                } else {
                    out.add(in.readBytes(length));
                }
                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }

}
