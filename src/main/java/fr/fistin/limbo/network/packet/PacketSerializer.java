package fr.fistin.limbo.network.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketSerializer {

    public static int readVarInt(ByteBuf buf) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static void writeVarInt(ByteBuf buf, int value) {
        do {
            byte temp = (byte)(value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buf.writeByte(temp);
        } while (value != 0);
    }

    public static int getVarIntSize(int paramInt) {
        if ((paramInt & 0xFFFFFF80) == 0)
            return 1;

        if ((paramInt & 0xFFFFC000) == 0)
            return 2;

        if ((paramInt & 0xFFE00000) == 0)
            return 3;

        if ((paramInt & 0xF0000000) == 0)
            return 4;

        return 5;
    }

    public static byte[] readByteArray(ByteBuf buf) {
        final int len = readVarInt(buf);
        final byte[] bytes = new byte[len];

        buf.readBytes(bytes);

        return bytes;
    }

    public static void writeByteArray(ByteBuf buf, byte[] bytes) {
        writeVarInt(buf, bytes.length);

        buf.writeBytes(bytes);
    }

    public static String readString(ByteBuf buf) {
        return new String(readByteArray(buf), StandardCharsets.UTF_8);
    }

    public static void writeString(ByteBuf buf, String str) {
        writeByteArray(buf, str.getBytes(StandardCharsets.UTF_8));
    }


}
