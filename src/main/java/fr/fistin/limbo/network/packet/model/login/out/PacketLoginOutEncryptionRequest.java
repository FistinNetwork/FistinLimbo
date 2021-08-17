package fr.fistin.limbo.network.packet.model.login.out;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.security.PublicKey;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 08:52
 */
public class PacketLoginOutEncryptionRequest extends PacketOutput {

    private final String serverId;
    private final PublicKey key;
    private final byte[] verifyToken;

    public PacketLoginOutEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken) {
        this.serverId = serverId;
        this.key = key;
        this.verifyToken = verifyToken;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        PacketSerializer.writeString(byteBuf, this.serverId);
        PacketSerializer.writeByteArray(byteBuf, this.key.getEncoded());
        PacketSerializer.writeByteArray(byteBuf, this.verifyToken);
    }

}
