package fr.fistin.limbo.network.packet.model.login;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 10:45
 */
public class PacketLoginOutDisconnect extends PacketOutput {

    private final String json;

    public PacketLoginOutDisconnect(String message) {
        this.json = "{\"text\":\"" + message.replaceAll("\"", "\\\"") + "\"}";
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        PacketSerializer.writeString(byteBuf, this.json);
    }

}
