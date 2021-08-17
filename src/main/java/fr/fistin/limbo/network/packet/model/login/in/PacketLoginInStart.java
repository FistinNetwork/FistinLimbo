package fr.fistin.limbo.network.packet.model.login.in;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutEncryptionRequest;
import fr.fistin.limbo.player.profile.GameProfile;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.util.UUIDUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 16:18
 */
public class PacketLoginInStart extends PacketInput {

    private String username;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.username = PacketSerializer.readString(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final Limbo limbo = playerConnection.getLimbo();
        final GameProfile profile = playerConnection.getProfile();

        profile.setUsername(this.username);

        if (profile.getUuid() == null) {
            profile.setUuid(UUIDUtil.getOffline(this.username));
        }

        playerConnection.sendPacket(new PacketLoginOutEncryptionRequest("", limbo.getKeyPair().getPublic(), limbo.getVerifyToken()));
    }

}
