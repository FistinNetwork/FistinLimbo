package fr.fistin.limbo.network.packet.model;

import fr.fistin.limbo.network.packet.PacketInput;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 17:48
 */
public class PacketInEmpty extends PacketInput {

    @Override
    public void read(ByteBuf byteBuf) throws IOException {}

}
