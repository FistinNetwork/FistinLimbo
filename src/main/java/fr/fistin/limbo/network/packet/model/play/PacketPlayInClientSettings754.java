package fr.fistin.limbo.network.packet.model.play;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 08:14
 */
public class PacketPlayInClientSettings754 extends PacketPlayInClientSettings107 {

    private boolean textFiltering;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        super.read(byteBuf);
        this.textFiltering = byteBuf.readBoolean();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        super.handlePacket(networkManager, playerConnection);

        this.settings.setTextFiltering(this.textFiltering);
    }

}
