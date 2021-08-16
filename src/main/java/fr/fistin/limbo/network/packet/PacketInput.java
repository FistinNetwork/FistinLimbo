package fr.fistin.limbo.network.packet;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.player.PlayerConnection;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 16:24
 */
public abstract class PacketInput extends Packet {

    public abstract void read(PacketSerializer packetSerializer) throws IOException;

    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {}

}
