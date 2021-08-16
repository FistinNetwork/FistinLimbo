package fr.fistin.limbo.network.packet.handshake;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 10:27
 */
public class PacketHandshake extends PacketInput {

    private ProtocolVersion version;
    private String handshakeAddress;
    private short handshakePort;
    private int nextState;

    @Override
    public void read(PacketSerializer packetSerializer) throws IOException {
        this.version = ProtocolVersion.getVersionById(packetSerializer.readVarInt());
        this.handshakeAddress = packetSerializer.readString();
        this.handshakePort = (short) packetSerializer.readUnsignedShort();
        this.nextState = packetSerializer.readVarInt();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final AbstractProtocol protocol = networkManager.getProtocolByVersion(this.version);
        final ProtocolState state = this.nextState == 1 || this.nextState == 2 ? ProtocolState.getStateById(this.nextState) : null;

        if (state != null) {
            playerConnection.setProtocol(protocol);
            playerConnection.setVersion(this.version);
            playerConnection.setState(state);
            playerConnection.setHandshakePort(this.handshakePort);
            playerConnection.setHandshakeAddress(this.handshakeAddress);
        } else {
            Limbo.getLogger().warning("Received invalid state: " + nextState);

            playerConnection.disconnect("State " + this.nextState + " incorrect");
        }
    }

}
