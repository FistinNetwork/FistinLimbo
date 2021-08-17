package fr.fistin.limbo.network.packet.model.play;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.player.settings.ChatMode;
import fr.fistin.limbo.player.settings.ClientSettings;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 22:40
 */
public class PacketPlayInClientSettings47 extends PacketInput {

    protected ClientSettings settings;

    private String locale;
    private byte viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private int displayedSkinParts;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.locale = PacketSerializer.readString(byteBuf);
        this.viewDistance = byteBuf.readByte();
        this.chatMode = ChatMode.getModeById(PacketSerializer.readVarInt(byteBuf));
        this.chatColors = byteBuf.readBoolean();
        this.displayedSkinParts = byteBuf.readUnsignedByte();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        this.settings = playerConnection.getSettings();

        this.settings.setLocale(this.locale);
        this.settings.setViewDistance(this.viewDistance);
        this.settings.setChatMode(this.chatMode);
        this.settings.setChatColors(this.chatColors);
        this.settings.setDisplayedSkinParts(this.displayedSkinParts);
    }

}
