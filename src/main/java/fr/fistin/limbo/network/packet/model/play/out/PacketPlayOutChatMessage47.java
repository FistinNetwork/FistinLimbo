package fr.fistin.limbo.network.packet.model.play.out;

import fr.fistin.limbo.chat.ChatPosition;
import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 21:37
 */
public class PacketPlayOutChatMessage47 extends PacketOutput {

    private final String json;
    private final ChatPosition position;

    public PacketPlayOutChatMessage47(String json, ChatPosition position) {
        this.json = json;
        this.position = position;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        PacketSerializer.writeString(byteBuf, this.json);
        byteBuf.writeByte((byte) this.position.getId());
    }

}
