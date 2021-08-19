package fr.fistin.limbo.network.protocol.ping;

import com.google.common.base.Charsets;
import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 18/08/2021 at 10:17
 */
public class LegacyPingHandler extends ChannelInboundHandlerAdapter {

    private final Limbo limbo;

    public LegacyPingHandler(Limbo limbo) {
        this.limbo = limbo;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        final ByteBuf byteBuf = (ByteBuf) msg;

        byteBuf.markReaderIndex();

        boolean flag = true;
        try {
            if (byteBuf.readUnsignedByte() == 0xFE) {
                final LimboConfiguration configuration = this.limbo.getConfiguration();
                final int players = this.limbo.getNetworkManager().getPlayers();
                final int maxSlots = configuration.getMaxSlots();
                final String motd = configuration.getMotd();

                final InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                final int readableBytes = byteBuf.readableBytes();

                String s;
                switch (readableBytes) {
                    case 0:
                        System.out.println("Ping: (< 1.3.x) from " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());

                        s = String.format("%s\u00a7%d\u00a7%d", motd, players, maxSlots);

                        this.sendAndClose(ctx, this.write(s));
                        break;
                    case 1:
                        if (byteBuf.readUnsignedByte() != 1) {
                            return;
                        }

                        System.out.println("Ping: (1.4-1.5.x) from " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());

                        s = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, "47", motd, players, maxSlots);

                        this.sendAndClose(ctx, this.write(s));
                        break;
                    default:
                        boolean flag1 = byteBuf.readUnsignedByte() == 1;

                        flag1 &= byteBuf.readUnsignedByte() == 250;
                        flag1 &= "MC|PingHost".equals(new String(byteBuf.readBytes(byteBuf.readShort() * 2).array(), Charsets.UTF_16BE));
                        int j = byteBuf.readUnsignedShort();

                        flag1 &= byteBuf.readUnsignedByte() >= 73;
                        flag1 &= 3 + byteBuf.readBytes(byteBuf.readShort() * 2).array().length + 4 == j;
                        flag1 &= byteBuf.readInt() <= '\uffff';
                        flag1 &= byteBuf.readableBytes() == 0;

                        if (flag1) {
                            System.out.println("Ping: (1.6) from " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());

                            final ByteBuf buf = this.write(String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, "47", motd, players, maxSlots));

                            try {
                                this.sendAndClose(ctx, buf);
                            } finally {
                                buf.release();
                            }
                        } else {
                            return;
                        }

                        byteBuf.release();
                        flag = false;
                }
            }
        } finally {
            if (flag) {
                byteBuf.resetReaderIndex();

                ctx.channel().pipeline().remove("legacy_query");
                ctx.fireChannelRead(msg);
            }
        }
    }

    private void sendAndClose(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        ctx.pipeline().firstContext().writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf write(String s) {
        final ByteBuf bytebuf = Unpooled.buffer();

        bytebuf.writeByte(255);

        final char[] chars = s.toCharArray();
        final int length = chars.length;

        bytebuf.writeShort(length);

        for (char aChar : chars) {
            bytebuf.writeChar(aChar);
        }

        return bytebuf;
    }

}
