package fr.fistin.limbo.network.packet;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 16:22
 */
public abstract class PacketOutput extends Packet {

    public abstract void write(PacketSerializer packetSerializer) throws IOException;

}
