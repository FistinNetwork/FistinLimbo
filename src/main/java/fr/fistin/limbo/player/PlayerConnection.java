package fr.fistin.limbo.player;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.model.PacketOutDisconnect;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import fr.fistin.limbo.network.protocol.encryption.EncryptingDecoder;
import fr.fistin.limbo.network.protocol.encryption.EncryptingEncoder;
import fr.fistin.limbo.network.protocol.encryption.EncryptionUtil;
import fr.fistin.limbo.player.profile.GameProfile;
import fr.fistin.limbo.player.settings.ClientSettings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 09:32
 */
public class PlayerConnection {

    private int keepAliveId;

    private ScheduledFuture<?> keepAliveFuture;

    private long lastKeepAlive;

    private AbstractProtocol protocol;
    private ProtocolState state;
    private ProtocolVersion version;

    private InetSocketAddress inetAddress;

    private final ClientSettings settings;

    private GameProfile profile;

    private final NetworkManager networkManager;

    private final Channel channel;
    private final Limbo limbo;

    public PlayerConnection(Limbo limbo, Channel channel) {
        this.limbo = limbo;
        this.channel = channel;
        this.networkManager = this.limbo.getNetworkManager();
        this.settings = new ClientSettings();
        this.inetAddress = (InetSocketAddress) channel.remoteAddress();
        this.state = ProtocolState.HANDSHAKE;
        this.protocol = this.networkManager.getProtocolByVersion(ProtocolVersion.HANDSHAKE);
        this.lastKeepAlive = System.currentTimeMillis();
        this.keepAliveId = 0;
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

                    this.disconnect("Version " + this.version.name().substring(9).replaceAll("_", ".") + " not supported");
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
            if (this.state != ProtocolState.STATUS) {
                this.sendPacket(new PacketOutDisconnect(reason));
            }
        }

        this.channel.close();
    }

    public void destroy() {
        if (this.keepAliveFuture != null) {
            this.keepAliveFuture.cancel(false);
        }
    }

    public void enableEncryption(SecretKey key) {
        this.channel.pipeline().addBefore("splitter", "decrypt", new EncryptingDecoder(EncryptionUtil.createNetCipherInstance(Cipher.DECRYPT_MODE, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new EncryptingEncoder(EncryptionUtil.createNetCipherInstance(Cipher.ENCRYPT_MODE, key)));
    }

    public void startKeepAliveFuture() {
        this.keepAliveFuture = this.channel.eventLoop().scheduleAtFixedRate(() -> this.protocol.sendKeepAlive(this, this.keepAliveId), 5, 10, TimeUnit.SECONDS);
    }

    public boolean isLocal() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
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

    public void setProfile(GameProfile profile) {
        this.profile = profile;
    }

    public ClientSettings getSettings() {
        return this.settings;
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

    public long getLastKeepAlive() {
        return this.lastKeepAlive;
    }

    public void setLastKeepAlive(long lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
    }

    public int getKeepAliveId() {
        return this.keepAliveId;
    }

    public void setKeepAliveId(int keepAliveId) {
        this.keepAliveId = keepAliveId;
    }

}
