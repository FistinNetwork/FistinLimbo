package fr.fistin.limbo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 19/08/2021 at 13:33
 */
public class ImageUtil {

    public static String encodeToBase64(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            final byte[] data = new byte[(int) file.length()];

            inputStream.read(data);

            return Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
