package fr.fistin.limbo.network.packet.model;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import fr.fistin.limbo.player.profile.GameProfile;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.util.UUIDUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 10:27
 */
public class PacketHandshake extends PacketInput {

    private ProtocolVersion version;
    private String handshakeAddress;
    private int nextState;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.version = ProtocolVersion.getVersionById(PacketSerializer.readVarInt(byteBuf));
        this.handshakeAddress = PacketSerializer.readString(byteBuf);

        byteBuf.readUnsignedShort(); // Server port

        this.nextState = PacketSerializer.readVarInt(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final AbstractProtocol protocol = networkManager.getProtocolByVersion(this.version);
        final ProtocolState state = this.nextState == 1 || this.nextState == 2 ? ProtocolState.getStateById(this.nextState) : null;

        if (state != null) {
            playerConnection.setProtocol(protocol);
            playerConnection.setVersion(this.version);
            playerConnection.setState(state);

            final String[] data = this.handshakeAddress.split("\00");

            InetSocketAddress bungeeAddress = null;
            if (data.length == 3 || data.length == 4) {
                this.handshakeAddress = data[0];

                bungeeAddress = playerConnection.getInetAddress();

                playerConnection.setInetAddress(new InetSocketAddress(data[1], playerConnection.getInetAddress().getPort()));
                playerConnection.setProfile(new GameProfile(UUIDUtil.fromString(data[2]), playerConnection.getProfile().getName()));
            }

            if (state == ProtocolState.LOGIN) {
                System.out.println("[" + playerConnection.getInetAddress().toString() + "] <-> New connection" + (bungeeAddress == null ? "" : " (via BungeeCord " + bungeeAddress.getHostString() + ")") + ".");
            }
        } else {
            Limbo.getLogger().warning("Received invalid state: " + nextState);

            playerConnection.disconnect("State " + this.nextState + " incorrect");
        }
    }

}
