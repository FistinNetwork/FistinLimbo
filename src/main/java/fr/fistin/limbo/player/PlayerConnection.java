package fr.fistin.limbo.player;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.model.login.PacketLoginOutDisconnect;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 09:32
 */
public class PlayerConnection {

    private AbstractProtocol protocol;
    private ProtocolState state;
    private ProtocolVersion version;

    private InetSocketAddress inetAddress;

    private final GameProfile profile;

    private final NetworkManager networkManager;

    private final Channel channel;
    private final Limbo limbo;

    public PlayerConnection(Limbo limbo, Channel channel) {
        this.limbo = limbo;
        this.channel = channel;
        this.networkManager = this.limbo.getNetworkManager();
        this.profile = new GameProfile();
        this.inetAddress = (InetSocketAddress) channel.remoteAddress();
        this.state = ProtocolState.HANDSHAKE;
        this.protocol = this.networkManager.getProtocolByVersion(ProtocolVersion.HANDSHAKE);
    }

    public void handlePacket(ByteBuf byteBuf) throws IOException {
        try {
            if (!(byteBuf instanceof EmptyByteBuf)) {
                final int packetId = PacketSerializer.readVarInt(byteBuf);

                if (this.protocol != null) {
                    final Class<? extends PacketInput> packetClass = this.protocol.getPacketInById(packetId, this.state);

                    if (packetClass != null) {
                        final PacketInput packet = packetClass.getDeclaredConstructor().newInstance();

                        packet.read(byteBuf);
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
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(PacketOutput packet) {
        final int packetId = this.protocol.getPacketOutByClass(packet.getClass(), this.state);

        if (packetId != -1) {
            packet.setId(packetId);

            this.channel.writeAndFlush(packet);
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

    public Limbo getLimbo() {
        return this.limbo;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public InetSocketAddress getInetAddress() {
        return this.inetAddress;
    }

    public void setInetAddress(InetSocketAddress inetAddress) {
        this.inetAddress = inetAddress;
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
