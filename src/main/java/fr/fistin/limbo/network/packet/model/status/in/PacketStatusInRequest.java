package fr.fistin.limbo.network.packet.model.status.in;

import com.google.gson.JsonObject;
import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.model.status.out.PacketStatusOutResponse;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.util.References;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Random;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 11:50
 */
public class PacketStatusInRequest extends PacketInput {

    @Override
    public void read(ByteBuf byteBuf) throws IOException {}

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final Limbo limbo = playerConnection.getLimbo();
        final LimboConfiguration configuration = limbo.getConfiguration();
        final JsonObject payload = new JsonObject();
        final JsonObject version = new JsonObject();
        final JsonObject players = new JsonObject();
        final JsonObject description = new JsonObject();

        version.addProperty("name", "FistinLimbo");
        version.addProperty("protocol", playerConnection.getVersion().getId());

        players.addProperty("max", configuration.getMaxSlots());
        players.addProperty("online", networkManager.getPlayers());

        description.addProperty("text", configuration.getMotd());

        payload.add("version", version);
        payload.add("players", players);
        payload.add("description", description);

        payload.addProperty("favicon", "data:image/png;base64," + limbo.getServerIcon());

        playerConnection.sendPacket(new PacketStatusOutResponse(payload.toString()));
    }

}
