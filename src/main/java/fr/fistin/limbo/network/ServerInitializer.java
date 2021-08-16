package fr.fistin.limbo.network;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.pipeline.PacketEncoder;
import fr.fistin.limbo.network.pipeline.VarInt21Decoder;
import fr.fistin.limbo.network.pipeline.VarInt21Encoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 15:05
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Limbo limbo;

    public ServerInitializer(Limbo limbo) {
        this.limbo = limbo;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("timeout", new ReadTimeoutHandler(30));

        ch.pipeline().addLast("var_int_decoder", new VarInt21Decoder());

        ch.pipeline().addLast("var_int_encoder", new VarInt21Encoder());
        ch.pipeline().addLast("packet_encoder", new PacketEncoder());

        ch.pipeline().addLast("handler", new ChannelHandler(this.limbo));
    }

}