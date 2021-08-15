package fr.fistin.limbo.packets.in;

import fr.fistin.limbo.NetworkManager;
import fr.fistin.limbo.packets.PacketSerializer;
import fr.fistin.limbo.PlayerConnection;
import fr.fistin.limbo.packets.out.PacketStatusOutResponse;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketStatusInRequest implements PacketIn
{
    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
        networkManager.sendPacket(playerConnection, new PacketStatusOutResponse("{\n" +
                "    \"version\": {\n" +
                "        \"name\": \"Limbo\",\n" +
                "        \"protocol\": " + playerConnection.getProtocolId() + "\n" +
                "    },\n" +
                "    \"players\": {\n" +
                "        \"max\": 9999,\n" +
                "        \"online\": 0,\n" +
                "        \"sample\": [\n" +
                "        ]\n" +
                "    },\t\n" +
                "    \"description\": {\n" +
                "        \"text\": \"Limbo v1\"\n" +
                "    }\n" +
                "}"));
    }
}
