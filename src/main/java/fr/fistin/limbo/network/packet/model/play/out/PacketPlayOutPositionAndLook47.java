package fr.fistin.limbo.network.packet.model.play.out;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.packets.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 19:58
 */
public class PacketPlayOutPositionAndLook47 extends PacketOutput {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final byte flags;

    public PacketPlayOutPositionAndLook47(double x, double y, double z, float yaw, float pitch, byte flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        byteBuf.writeDouble(this.x);
        byteBuf.writeDouble(this.y);
        byteBuf.writeDouble(this.z);
        byteBuf.writeFloat(this.yaw);
        byteBuf.writeFloat(this.pitch);
        byteBuf.writeByte(this.flags);
    }

}
