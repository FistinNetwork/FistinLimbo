package fr.fistin.limbo.network.packet.login;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 16:18
 */
public class PacketLoginInStart extends PacketInput {

    private String username;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.username = PacketSerializer.readString(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        playerConnection.getProfile().setUsername(this.username);

        playerConnection.sendPacket(new PacketLoginOutSuccess(playerConnection.getProfile()));
    }

}
