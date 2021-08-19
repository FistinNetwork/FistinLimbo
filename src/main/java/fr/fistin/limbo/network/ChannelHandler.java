package fr.fistin.limbo.network;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.player.PlayerConnection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 18:39
 */
public class ChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private PlayerConnection playerConnection;

    private final Limbo limbo;

    public ChannelHandler(Limbo limbo) {
        this.limbo = limbo;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.playerConnection = new PlayerConnection(this.limbo, ctx.channel());

        this.limbo.getNetworkManager().getPlayersConnections().add(this.playerConnection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.dispatchSession();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws IOException {
        this.playerConnection.handlePacket(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        this.dispatchSession();

        Limbo.getLogger().log(Level.WARNING, "Player error.", cause);
    }

    private void dispatchSession() {
        if (this.playerConnection != null) {
            final NetworkManager networkManager = this.limbo.getNetworkManager();

            networkManager.getPlayersConnections().remove(this.playerConnection);

            if (this.playerConnection.getState() == ProtocolState.PLAY) {
                networkManager.setPlayers(networkManager.getPlayers() - 1);

                final String message = this.playerConnection.getProfile().getName() + " left the game";

                System.out.println(message);

                networkManager.sendMessageToAllPlayers(message);
            }

            this.playerConnection.destroy();
        }
    }

}
