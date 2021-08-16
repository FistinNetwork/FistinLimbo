package fr.fistin.limbo.network.packet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 16:22
 */
public abstract class PacketOutput extends Packet {

    public abstract void write(ByteBuf byteBuf) throws IOException;

}
