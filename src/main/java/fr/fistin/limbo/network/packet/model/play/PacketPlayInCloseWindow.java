package fr.fistin.limbo.network.packet.model.play;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 08:35
 */
public class PacketPlayInCloseWindow extends PacketInput {

    private int inventoryId;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.inventoryId = byteBuf.readUnsignedByte();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {}

}
