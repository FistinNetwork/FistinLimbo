package fr.fistin.limbo.network.packet.model.play;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 17:06
 */
public class PacketPlayOutJoinGame47 extends PacketOutput {

    private final int entityId;
    private final byte gamemode;
    private final byte dimension;
    private final byte difficulty;
    private final byte maxPlayers;
    private final String levelType;
    private final boolean debugInfo;

    public PacketPlayOutJoinGame47(int entityId, byte gamemode, byte dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo) {
        this.entityId = entityId;
        this.gamemode = gamemode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.debugInfo = debugInfo;
    }

    @Override
    public void write(ByteBuf byteBuf) throws IOException {
        byteBuf.writeInt(this.entityId);
        byteBuf.writeByte(this.gamemode);
        byteBuf.writeByte(this.dimension);
        byteBuf.writeByte(this.difficulty);
        byteBuf.writeByte(this.maxPlayers);
        PacketSerializer.writeString(byteBuf, this.levelType);
        byteBuf.writeBoolean(this.debugInfo);
    }

}
