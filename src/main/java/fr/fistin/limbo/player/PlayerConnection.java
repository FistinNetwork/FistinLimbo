package fr.fistin.limbo.player;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.login.PacketLoginOutDisconnect;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 09:32
 */
public class PlayerConnection {

    private AbstractProtocol protocol;
    private ProtocolState state;
    private ProtocolVersion version;

    private String handshakeAddress;
    private int handshakePort;

    private final GameProfile profile;

    private final NetworkManager networkManager;

    private final Channel channel;
    private final Limbo limbo;

    public PlayerConnection(Limbo limbo, Channel channel) {
        this.limbo = limbo;
        this.channel = channel;
        this.networkManager = this.limbo.getNetworkManager();
        this.profile = new GameProfile();
        this.state = ProtocolState.HANDSHAKE;
        this.protocol = this.networkManager.getProtocolByVersion(ProtocolVersion.HANDSHAKE);
    }

    public void handlePacket(ByteBuf byteBuf) throws IOException {
        try {
            if (!(byteBuf instanceof EmptyByteBuf)) {
                final PacketSerializer packetSerializer = new PacketSerializer(byteBuf);
                final int packetId = packetSerializer.readVarInt();

                if (this.protocol != null) {
                    final Class<? extends PacketInput> packetClass = this.protocol.getPacketInById(packetId, this.state);

                    System.out.println("Receiving " + packetClass.getSimpleName());

                    if (packetClass != null) {
                        final PacketInput packet = packetClass.newInstance();

                        packet.read(packetSerializer);
                        packet.handlePacket(this.networkManager, this);
                    } else {
                        Limbo.getLogger().warning("Received unknown packet " + packetId + " (0x" + (char)(packetId / 16 > 9 ? packetId / 16 + 'A' - 10 : packetId / 16 + '0') + "" + (char)(packetId % 16 > 9 ? packetId % 16 + 'A' - 10 : packetId % 16 + '0') + ").");
                    }
                } else {
                    this.protocol = this.networkManager.getProtocolByVersion(ProtocolVersion.HANDSHAKE);
                    this.state = ProtocolState.HANDSHAKE;

                    Limbo.getLogger().warning("Version " + this.version + " not supported");

                    this.disconnect("Version " + this.version + " not supported");
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(PacketOutput packet) {
        final int packetId = this.protocol.getPacketOutByClass(packet.getClass(), this.state);

        if (packetId != -1) {
            packet.setId(packetId);

            this.channel.writeAndFlush(packet);

            System.out.println("Sending " + packet.getClass().getSimpleName());
        } else {
            Limbo.getLogger().warning("Sending unknown packet " + packet.getClass().getSimpleName());
        }
    }

    public void disconnect(String reason) {
        if (reason != null) {
            if (this.state == ProtocolState.HANDSHAKE || this.state == ProtocolState.LOGIN) {
                this.sendPacket(new PacketLoginOutDisconnect(reason));
            }
        }
    }

    public void destroy() {

    }

    public Channel getChannel() {
        return this.channel;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public int getHandshakePort() {
        return this.handshakePort;
    }

    public void setHandshakePort(int handshakePort) {
        this.handshakePort = handshakePort;
    }

    public String getHandshakeAddress() {
        return this.handshakeAddress;
    }

    public void setHandshakeAddress(String handshakeAddress) {
        this.handshakeAddress = handshakeAddress;
    }

    public ProtocolVersion getVersion() {
        return this.version;
    }

    public void setVersion(ProtocolVersion version) {
        this.version = version;
    }

    public ProtocolState getState() {
        return this.state;
    }

    public void setState(ProtocolState state) {
        this.state = state;
    }

    public AbstractProtocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(AbstractProtocol protocol) {
        this.protocol = protocol;
    }

}
