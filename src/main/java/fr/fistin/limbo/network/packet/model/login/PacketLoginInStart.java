package fr.fistin.limbo.network.packet.model.login;

import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.protocol.ProtocolState;
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
        playerConnection.setState(ProtocolState.PLAY);

        this.sendJoinGamePacket(playerConnection);
    }

    private void sendJoinGamePacket(PlayerConnection playerConnection) {
        final LimboConfiguration configuration = playerConnection.getLimbo().getConfiguration();
        final byte gamemode = configuration.getGameMode();
        final byte dimension = configuration.getDimension();
        final boolean reducedDebugInfo = configuration.isReducedDebugInfo();

        playerConnection.getProtocol().sendJoinGame(playerConnection, 1, gamemode, dimension, (byte) 0, (byte) 1, "default", reducedDebugInfo);
    }

}
