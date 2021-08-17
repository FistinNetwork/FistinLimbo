package fr.fistin.limbo.network.packet.model.play.in;

import com.google.gson.JsonObject;
import fr.fistin.limbo.chat.ChatPosition;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.model.play.out.PacketPlayOutChatMessage47;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 21:34
 */
public class PacketPlayInChatMessage extends PacketInput {

    private String message;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.message = PacketSerializer.readString(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final String formattedMessage = playerConnection.getProfile().getName() + ": " + this.message;
        final JsonObject json = new JsonObject();

        json.addProperty("text", formattedMessage);

        playerConnection.sendPacket(new PacketPlayOutChatMessage47(json.toString(), ChatPosition.CHAT));
    }

}
