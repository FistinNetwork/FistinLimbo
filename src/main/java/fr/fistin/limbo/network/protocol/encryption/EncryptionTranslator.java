package fr.fistin.limbo.network.protocol.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 14:01
 */
public class EncryptionTranslator {

    private byte[] a = new byte[0];
    private byte[] b = new byte[0];

    private final Cipher cipher;

    public EncryptionTranslator(Cipher cipher) {
        this.cipher = cipher;
    }

    private byte[] read(ByteBuf byteBuf) {
        final int i = byteBuf.readableBytes();

        if (this.a.length < i) {
            this.a = new byte[i];
        }

        byteBuf.readBytes(this.a, 0, i);

        return this.a;
    }

    protected ByteBuf decipher(ChannelHandlerContext ctx, ByteBuf byteBuf) throws ShortBufferException {
        final int i = byteBuf.readableBytes();
        final byte[] aByte = this.read(byteBuf);
        final ByteBuf bytebuf = ctx.alloc().heapBuffer(this.cipher.getOutputSize(i));

        bytebuf.writerIndex(this.cipher.update(aByte, 0, i, bytebuf.array(), bytebuf.arrayOffset()));

        return bytebuf;
    }

    protected void cipher(ByteBuf in, ByteBuf out) throws ShortBufferException {
        final int i = in.readableBytes();
        final byte[] buffer  = this.read(in);
        final int j = this.cipher.getOutputSize(i);

        if (this.b.length < j) {
            this.b = new byte[j];
        }

        out.writeBytes(this.b, 0, this.cipher.update(buffer, 0, i, this.b));
    }

}
