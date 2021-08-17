package fr.fistin.limbo.network.packet.model.login.in;

import com.mojang.authlib.GameProfile;
import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.LimboConfiguration;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.packet.PacketInput;
import fr.fistin.limbo.network.packet.PacketSerializer;
import fr.fistin.limbo.network.packet.model.login.out.PacketLoginOutSuccess;
import fr.fistin.limbo.network.protocol.ProtocolState;
import fr.fistin.limbo.player.PlayerConnection;
import fr.fistin.limbo.network.protocol.encryption.EncryptionUtil;
import fr.fistin.limbo.world.Chunk;
import io.netty.buffer.ByteBuf;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 08:52
 */
public class PacketLoginInEncryptionResponse extends PacketInput {

    private byte[] sharedSecretEncrypted;
    private byte[] verifyTokenEncrypted;

    @Override
    public void read(ByteBuf byteBuf) throws IOException {
        this.sharedSecretEncrypted = PacketSerializer.readByteArray(byteBuf);
        this.verifyTokenEncrypted = PacketSerializer.readByteArray(byteBuf);
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection) {
        final Limbo limbo = playerConnection.getLimbo();
        final PrivateKey privateKey = limbo.getKeyPair().getPrivate();

        if (Arrays.equals(limbo.getVerifyToken(), this.getVerifyToken(privateKey))) {
            networkManager.getLoginManager().auth(playerConnection, this.getSecretKey(privateKey));
        } else {
            throw new IllegalStateException("Invalid verify token!");
        }
    }

    private SecretKey getSecretKey(PrivateKey key) {
        return EncryptionUtil.decryptSharedKey(key, this.sharedSecretEncrypted);
    }

    private byte[] getVerifyToken(PrivateKey key) {
        return key == null ? this.verifyTokenEncrypted : EncryptionUtil.decryptData(key, this.verifyTokenEncrypted);
    }

}
