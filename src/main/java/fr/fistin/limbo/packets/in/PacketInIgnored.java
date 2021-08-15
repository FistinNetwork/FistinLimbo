package fr.fistin.limbo.packets.in;

import fr.fistin.limbo.NetworkManager;
import fr.fistin.limbo.packets.PacketSerializer;
import fr.fistin.limbo.PlayerConnection;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketInIgnored implements PacketIn
{
    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
    }
}
