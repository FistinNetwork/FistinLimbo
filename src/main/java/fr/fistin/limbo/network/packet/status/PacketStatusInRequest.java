package fr.fistin.limbo.network.packet.status;

import com.google.gson.JsonObject;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 11:50
 */
public class PacketStatusInRequest extends PacketInput {

    @Override
    public void read(PacketSerializer packetSerializer) throws IOException {}

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final JsonObject payload = new JsonObject();
        final JsonObject version = new JsonObject();
        final JsonObject players = new JsonObject();
        final JsonObject description = new JsonObject();

        version.addProperty("name", "Limbo");
        version.addProperty("protocol", playerConnection.getVersion().getId());

        players.addProperty("max", 9999);
        players.addProperty("online", 1200);

        description.addProperty("text", "FistinLimbo");

        payload.add("version", version);
        payload.add("players", players);
        payload.add("description", description);

        playerConnection.sendPacket(new PacketStatusOutResponse(payload.toString()));
    }
}
