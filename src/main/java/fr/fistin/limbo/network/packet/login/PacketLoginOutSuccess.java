package fr.fistin.limbo.network.packet.login;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.player.GameProfile;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 16:37
 */
public class PacketLoginOutSuccess extends PacketOutput {

    private final GameProfile profile;

    public PacketLoginOutSuccess(GameProfile profile) {
        this.profile = profile;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        PacketSerializer.writeString(byteBuf, profile.getUuid().toString());
        PacketSerializer.writeString(byteBuf, profile.getUsername());
    }

}
