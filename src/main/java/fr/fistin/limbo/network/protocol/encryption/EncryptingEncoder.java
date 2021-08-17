package fr.fistin.limbo.network.protocol.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 14:00
 */
public class EncryptingEncoder extends MessageToByteEncoder<ByteBuf> {

    private final EncryptionTranslator encryptionTranslator;

    public EncryptingEncoder(Cipher cipher) {
        this.encryptionTranslator = new EncryptionTranslator(cipher);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        this.encryptionTranslator.cipher(msg, out);
    }

}
