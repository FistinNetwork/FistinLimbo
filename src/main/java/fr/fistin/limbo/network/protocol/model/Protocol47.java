package fr.fistin.limbo.network.protocol.model;

import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.model.PacketInEmpty;
import fr.fistin.limbo.network.packet.model.PacketOutDisconnect;
import fr.fistin.limbo.network.packet.model.login.in.PacketLoginInEncryptionResponse;
import fr.fistin.limbo.network.packet.model.login.in.PacketLoginInStart;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutEncryptionRequest;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutSuccess;
import fr.fistin.limbo.network.packet.model.play.in.PacketPlayInChatMessage;
import fr.fistin.limbo.network.packet.model.play.in.PacketPlayInClientSettings47;
import fr.fistin.limbo.network.packet.model.play.in.PacketPlayInCloseWindow;
import fr.fistin.limbo.network.packet.model.play.in.PacketPlayInKeepAlive47;
import fr.fistin.limbo.network.packet.model.play.out.*;
import fr.fistin.limbo.network.packet.model.status.in.PacketStatusInPing;
import fr.fistin.limbo.network.packet.model.status.in.PacketStatusInRequest;
import fr.fistin.limbo.network.packet.model.status.out.PacketStatusOutPong;
import fr.fistin.limbo.network.packet.model.status.out.PacketStatusOutResponse;
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
        this.registerPacketIn(ProtocolState.LOGIN, 0x01, PacketLoginInEncryptionResponse.class);

        this.registerPacketOut(ProtocolState.LOGIN, 0x00, PacketOutDisconnect.class);
        this.registerPacketOut(ProtocolState.LOGIN, 0x01, PacketLoginOutEncryptionRequest.class);
        this.registerPacketOut(ProtocolState.LOGIN, 0x02, PacketLoginOutSuccess.class);
    }

    private void registerPlay() {
        this.registerPacketOut(ProtocolState.PLAY, 0x00, PacketPlayOutKeepAlive47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x01, PacketPlayOutJoinGame47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x02, PacketPlayOutChatMessage47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x08, PacketPlayOutPositionAndLook47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x21, PacketPlayOutChunkData47.class);
        this.registerPacketOut(ProtocolState.PLAY, 0x40, PacketOutDisconnect.class);

        this.registerPacketIn(ProtocolState.PLAY, 0x00, PacketPlayInKeepAlive47.class);
        this.registerPacketIn(ProtocolState.PLAY, 0x01, PacketPlayInChatMessage.class); //Chat message
        this.registerPacketIn(ProtocolState.PLAY, 0x02, PacketInEmpty.class); //Use entity
        this.registerPacketIn(ProtocolState.PLAY, 0x03, PacketInEmpty.class); //Player (ground)
        this.registerPacketIn(ProtocolState.PLAY, 0x04, PacketInEmpty.class); //Player position
        this.registerPacketIn(ProtocolState.PLAY, 0x05, PacketInEmpty.class); //Player look
        this.registerPacketIn(ProtocolState.PLAY, 0x06, PacketInEmpty.class); //Player position and look
        this.registerPacketIn(ProtocolState.PLAY, 0x07, PacketInEmpty.class); //Player digging
        this.registerPacketIn(ProtocolState.PLAY, 0x08, PacketInEmpty.class); //Block placement
        this.registerPacketIn(ProtocolState.PLAY, 0x09, PacketInEmpty.class); //Held item change
        this.registerPacketIn(ProtocolState.PLAY, 0x0A, PacketInEmpty.class); //Animation
        this.registerPacketIn(ProtocolState.PLAY, 0x0B, PacketInEmpty.class); //Entity action
        this.registerPacketIn(ProtocolState.PLAY, 0x0C, PacketInEmpty.class); //Steer vehicle
        this.registerPacketIn(ProtocolState.PLAY, 0x0D, PacketPlayInCloseWindow.class); //Close window
        this.registerPacketIn(ProtocolState.PLAY, 0x0E, PacketInEmpty.class); //Click window
        this.registerPacketIn(ProtocolState.PLAY, 0x0F, PacketInEmpty.class); //Confirm transaction
        this.registerPacketIn(ProtocolState.PLAY, 0x10, PacketInEmpty.class); //Creative inventory action
        this.registerPacketIn(ProtocolState.PLAY, 0x11, PacketInEmpty.class); //Enchant item
        this.registerPacketIn(ProtocolState.PLAY, 0x12, PacketInEmpty.class); //Update sign
        this.registerPacketIn(ProtocolState.PLAY, 0x13, PacketInEmpty.class); //Player abilities
        this.registerPacketIn(ProtocolState.PLAY, 0x14, PacketInEmpty.class); //Tab complete
        this.registerPacketIn(ProtocolState.PLAY, 0x15, PacketPlayInClientSettings47.class);
        this.registerPacketIn(ProtocolState.PLAY, 0x16, PacketInEmpty.class); //Client status
        this.registerPacketIn(ProtocolState.PLAY, 0x17, PacketInEmpty.class); //Plugin message
        this.registerPacketIn(ProtocolState.PLAY, 0x18, PacketInEmpty.class); //Spectate
        this.registerPacketIn(ProtocolState.PLAY, 0x19, PacketInEmpty.class); //Resource pack status
    }

    @Override
    public void sendJoinGame(PlayerConnection playerConnection, int entityId, byte gameMode, byte dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo) {
        playerConnection.sendPacket(new PacketPlayOutJoinGame47(entityId, gameMode, dimension, difficulty, maxPlayers, levelType, debugInfo));
    }

    @Override
    public void sendPosition(PlayerConnection playerConnection, double x, double y, double z, float yaw, float pitch) {
        playerConnection.sendPacket(new PacketPlayOutPositionAndLook47(x, y, z, yaw, pitch, (byte) 0));
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
        playerConnection.sendPacket(new PacketPlayOutChunkData47(chunk));
    }

    @Override
    public ProtocolVersion[] getVersions() {
        return new ProtocolVersion[] {ProtocolVersion.PROTOCOL_1_8};
    }

}
