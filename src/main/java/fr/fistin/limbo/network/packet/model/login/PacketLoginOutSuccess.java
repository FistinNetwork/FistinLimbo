package fr.fistin.limbo.network.packet.model.login;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 16:37
 */
public class PacketLoginOutSuccess extends PacketOutput {

    private final String username;
    private final UUID uuid;

    public PacketLoginOutSuccess(String username, UUID uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        PacketSerializer.writeString(byteBuf, this.uuid.toString());
        PacketSerializer.writeString(byteBuf, this.username);
    }

}
