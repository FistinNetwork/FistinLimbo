package fr.fistin.limbo.network.protocol.encryption;

import fr.fistin.limbo.Limbo;
import org.checkerframework.checker.units.qual.K;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.logging.Level;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 09:09
 */
public class EncryptionUtil {

    public static byte[] digestKey(String s, PublicKey publickey, SecretKey secretkey) {
        try {
            return digestData("SHA-1", s.getBytes("ISO_8859_1"), secretkey.getEncoded(), publickey.getEncoded());
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            unsupportedencodingexception.printStackTrace();
        }
        return null;
    }

    private static byte[] digestData(String s, byte[]... aByte) {
        try {
            final MessageDigest messagedigest = MessageDigest.getInstance(s);
            final int i = aByte.length;

            for (int j = 0; j < i; ++j) {
                final byte[] anOtherByte = aByte[j];

                messagedigest.update(anOtherByte);
            }

            return messagedigest.digest();
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
        }
        return null;
    }

    public static KeyPair generateKeyPair() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(1024);

            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            Limbo.getLogger().log(Level.SEVERE, "Key pair generation failed!", e);
        }
        return null;
    }

    public static byte[] decryptData(Key key, byte[] data) {
        return cipherOperation(Cipher.DECRYPT_MODE, key, data);
    }

    public static SecretKey decryptSharedKey(PrivateKey key, byte[] secretKeyEncrypted) {
        return new SecretKeySpec(decryptData(key, secretKeyEncrypted), "AES");
    }

    private static byte[] cipherOperation(int operationMode, Key key, byte[] data) {
        try {
            return createCipherInstance(operationMode, key.getAlgorithm(), key).doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();

            System.err.println("Cipher data failed!");
        }
        return null;
    }

    private static Cipher createCipherInstance(int operationMode, String transformation, Key key) {
        try {
            final Cipher cipher = Cipher.getInstance(transformation);

            cipher.init(operationMode, key);

            return cipher;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();

            System.err.println("Cipher creation failed!");
        }
        return null;
    }

    public static Cipher createNetCipherInstance(int operationMode, Key key) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");

            cipher.init(operationMode, key, new IvParameterSpec(key.getEncoded()));

            return cipher;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();

            System.err.println("Cipher creation failed!");
        }
        return null;
    }

}
