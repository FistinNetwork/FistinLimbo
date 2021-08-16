package fr.fistin.limbo.network.protocol.model;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.model.PacketHandshake;
import fr.fistin.limbo.network.packet.model.login.PacketLoginOutDisconnect;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.world.Chunk;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 10:23
 */
public class ProtocolHandshake extends AbstractProtocol {

    public ProtocolHandshake(NetworkManager networkManager) {
        super(networkManager);

        this.registerPacketIn(ProtocolState.HANDSHAKE, 0x00, PacketHandshake.class);

        this.registerPacketOut(ProtocolState.HANDSHAKE, 0x00, PacketLoginOutDisconnect.class);
    }

    @Override
    public void sendJoinGame(PlayerConnection playerConnection, int entityId, byte gameMode, byte dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo) {}

    @Override
    public void sendPosition(PlayerConnection playerConnection, double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void sendKeepAlive(PlayerConnection playerConnection, int id) {}

    @Override
    public void sendChunk(PlayerConnection playerConnection, Chunk chunk) {}

    @Override
    public ProtocolVersion[] getVersions() {
        return new ProtocolVersion[] {ProtocolVersion.HANDSHAKE};
    }

}
