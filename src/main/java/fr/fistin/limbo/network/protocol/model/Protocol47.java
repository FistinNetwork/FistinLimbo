package fr.fistin.limbo.network.protocol.model;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.model.PacketInEmpty;
import fr.fistin.limbo.network.packet.model.PacketOutDisconnect;
import fr.fistin.limbo.network.packet.model.login.PacketLoginInStart;
import fr.fistin.limbo.network.packet.model.login.PacketLoginOutSuccess;
import fr.fistin.limbo.network.packet.model.play.PacketPlayInKeepAlive47;
import fr.fistin.limbo.network.packet.model.play.PacketPlayOutJoinGame47;
import fr.fistin.limbo.network.packet.model.play.PacketPlayOutKeepAlive47;
import fr.fistin.limbo.network.packet.model.status.PacketStatusInPing;
import fr.fistin.limbo.network.packet.model.status.PacketStatusInRequest;
import fr.fistin.limbo.network.packet.model.status.PacketStatusOutPong;
import fr.fistin.limbo.network.packet.model.status.PacketStatusOutResponse;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.world.Chunk;

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
        this.registerPlay();
    }

    private void registerStatus() {
        this.registerPacketIn(ProtocolState.STATUS, 0x00, PacketStatusInRequest.class);
        this.registerPacketIn(ProtocolState.STATUS, 0x01, PacketStatusInPing.class);

        this.registerPacketOut(ProtocolState.STATUS, 0x00, PacketStatusOutResponse.class);
        this.registerPacketOut(ProtocolState.STATUS, 0x01, PacketStatusOutPong.class);
    }

    private void registerLogin() {
        this.registerPacketIn(ProtocolState.LOGIN, 0x00, PacketLoginInStart.class);

        this.registerPacketOut(ProtocolState.LOGIN, 0x00, PacketOutDisconnect.class);
        this.registerPacketOut(ProtocolState.LOGIN, 0x02, PacketLoginOutSuccess.class);
    }

    private void registerPlay() {
        this.registerPacketOut(ProtocolState.PLAY, 0x00, PacketPlayOutKeepAlive47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x01, PacketPlayOutJoinGame47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x40, PacketOutDisconnect.class);

        this.registerPacketIn(ProtocolState.PLAY, 0x00, PacketPlayInKeepAlive47.class);
        this.registerPacketIn(ProtocolState.PLAY, 0x04, PacketInEmpty.class);
    }

    @Override
    public void sendJoinGame(PlayerConnection playerConnection, int entityId, byte gameMode, byte dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo) {
        playerConnection.sendPacket(new PacketPlayOutJoinGame47(entityId, gameMode, dimension, difficulty, maxPlayers, levelType, debugInfo));
    }

    @Override
    public void sendPosition(PlayerConnection playerConnection, double x, double y, double z, float yaw, float pitch) {

    }

    @Override
    public void sendKeepAlive(PlayerConnection playerConnection, int id) {
        playerConnection.setKeepAliveId(id + 1);

        final long time = System.currentTimeMillis();
        final long supposedOldTime = time - 30000L;

        if (playerConnection.getLastKeepAlive() < supposedOldTime) {
            playerConnection.disconnect("Timed out");
        } else {
            playerConnection.sendPacket(new PacketPlayOutKeepAlive47(playerConnection.getKeepAliveId()));
        }
    }

    @Override
    public void sendChunk(PlayerConnection playerConnection, Chunk chunk) {

    }

    @Override
    public ProtocolVersion[] getVersions() {
        return new ProtocolVersion[] {ProtocolVersion.PROTOCOL_1_8};
    }

}
