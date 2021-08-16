package fr.fistin.limbo.network.protocol.model;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.login.PacketLoginInStart;
import fr.fistin.limbo.network.packet.login.PacketLoginOutSuccess;
import fr.fistin.limbo.network.packet.status.PacketStatusInPing;
import fr.fistin.limbo.network.packet.status.PacketStatusInRequest;
import fr.fistin.limbo.network.packet.status.PacketStatusOutPong;
import fr.fistin.limbo.network.packet.status.PacketStatusOutResponse;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 11:37
 */
public class Protocol47 extends AbstractProtocol {

    public Protocol47(NetworkManager networkManager) {
        super(networkManager);

        this.registerStatus();
        this.registerLogin();
    }

    private void registerStatus() {
        this.registerPacketIn(ProtocolState.STATUS, 0x00, PacketStatusInRequest.class);
        this.registerPacketIn(ProtocolState.STATUS, 0x01, PacketStatusInPing.class);

        this.registerPacketOut(ProtocolState.STATUS, 0x00, PacketStatusOutResponse.class);
        this.registerPacketOut(ProtocolState.STATUS, 0x01, PacketStatusOutPong.class);
    }

    private void registerLogin() {
        this.registerPacketIn(ProtocolState.LOGIN, 0x00, PacketLoginInStart.class);

        this.registerPacketOut(ProtocolState.LOGIN, 0x02, PacketLoginOutSuccess.class);
    }

    @Override
    public ProtocolVersion[] getVersions() {
        return new ProtocolVersion[] {ProtocolVersion.PROTOCOL_1_8};
    }

}
