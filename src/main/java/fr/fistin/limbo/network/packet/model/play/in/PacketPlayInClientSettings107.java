package fr.fistin.limbo.network.packet.model.play.in;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.player.settings.MainHand;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 08:14
 */
public class PacketPlayInClientSettings107 extends PacketPlayInClientSettings47 {

    private MainHand mainHand;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        super.read(byteBuf);
        this.mainHand = MainHand.getMainHandById(PacketSerializer.readVarInt(byteBuf));
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        super.handlePacket(networkManager, playerConnection);

        this.settings.setMainHand(this.mainHand);
    }
}
