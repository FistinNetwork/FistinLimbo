package fr.fistin.limbo.network;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.protocol.AbstractProtocol;
import fr.fistin.limbo.network.protocol.ProtocolVersion;
import fr.fistin.limbo.network.protocol.login.LoginManager;
import fr.fistin.limbo.network.protocol.model.Protocol47;
import fr.fistin.limbo.network.protocol.model.ProtocolHandshake;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 14:52
 */
public class NetworkManager {

    private EventLoopGroup bossGroup;

    private final LoginManager loginManager;

    private final List<AbstractProtocol> protocols;
    private final List<PlayerConnection> playersConnections;

    private final Limbo limbo;

    public NetworkManager(Limbo limbo) {
        this.limbo = limbo;
        this.playersConnections = new ArrayList<>();
        this.protocols = new ArrayList<>();
        this.loginManager = new LoginManager(this.limbo);

        this.protocols.add(new ProtocolHandshake(this));
        this.protocols.add(new Protocol47(this));
    }

    public void start() {
        System.out.println("Starting Limbo network manager...");

        Class<? extends ServerChannel> channelClass;
        if (Epoll.isAvailable()) {
            this.bossGroup = new EpollEventLoopGroup();

            channelClass = EpollServerSocketChannel.class;

            System.out.println("Epoll is available, so it will be used.");
        } else {
            this.bossGroup = new NioEventLoopGroup();

            channelClass = NioServerSocketChannel.class;

            System.out.println("Cannot use Epoll transport, so NIO will be used as a transport instead. Reason: " + Epoll.unavailabilityCause().getMessage());
        }

        final ServerBootstrap bootstrap = new ServerBootstrap()
                .group(this.bossGroup)
                .channel(channelClass)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ServerInitializer(this.limbo));

        try {
            final Channel channel = bootstrap.bind(limbo.getConfiguration().getIp(), limbo.getConfiguration().getPort()).sync().channel();

            System.out.println("Listening on " + channel.localAddress().toString());

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            System.err.println("Failed to bind to port!");
            System.err.println("Make sure that no other applications are using the port given in server.properties.");
            System.err.println("Exception: " + e.getMessage());

            System.exit(1);
        }
    }

    public void shutdown() {
        System.out.println("Stopping Limbo network manager...");

        this.bossGroup.shutdownGracefully();
    }

    public List<PlayerConnection> getPlayersConnections() {
        return this.playersConnections;
    }

    public List<AbstractProtocol> getProtocols() {
        return this.protocols;
    }

    public AbstractProtocol getProtocolByVersion(ProtocolVersion version) {
        if (version != null) {
            for (AbstractProtocol protocol : this.protocols) {
                for (ProtocolVersion v : protocol.getVersions()) {
                    if (v == version) {
                        return protocol;
                    }
                }
            }
        }
        return null;
    }

    public LoginManager getLoginManager() {
        return this.loginManager;
    }

}
