package fr.fistin.limbo.network.packet.status;

import fr.fistin.limbo.network.packet.PacketOutput;
import fr.fistin.limbo.network.packet.PacketSerializer;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 11:52
 */
public class PacketStatusOutResponse extends PacketOutput {

    private final String response;

    public PacketStatusOutResponse(String response) {
        this.response = response;
    }

    @Override
    public void write(PacketSerializer packetSerializer) throws IOException {
        packetSerializer.writeString(this.response);
    }

}
