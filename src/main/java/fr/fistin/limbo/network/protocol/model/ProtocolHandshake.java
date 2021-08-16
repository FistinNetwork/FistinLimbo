package fr.fistin.limbo.network.protocol.model;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.handshake.PacketHandshake;
import fr.fistin.limbo.network.packet.login.PacketLoginOutDisconnect;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;

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
    public ProtocolVersion[] getVersions() {
        return new ProtocolVersion[] {ProtocolVersion.HANDSHAKE};
    }

}
