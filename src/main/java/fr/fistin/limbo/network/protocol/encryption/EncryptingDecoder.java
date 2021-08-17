package fr.fistin.limbo.network.protocol.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.crypto.Cipher;
import java.util.List;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 14:43
 */
public class EncryptingDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final EncryptionTranslator encryptionTranslator;

    public EncryptingDecoder(Cipher cipher) {
        this.encryptionTranslator = new EncryptionTranslator(cipher);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(this.encryptionTranslator.decipher(ctx, msg));
    }

}
