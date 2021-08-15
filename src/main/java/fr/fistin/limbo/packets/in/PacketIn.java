package fr.fistin.limbo.packets.in;

import fr.fistin.limbo.NetworkManager;
import fr.fistin.limbo.packets.PacketSerializer;
import fr.fistin.limbo.PlayerConnection;

import java.io.IOException;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public interface PacketIn
{
    void readPacket(PacketSerializer packetSerializer) throws IOException;

    void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection);
}
