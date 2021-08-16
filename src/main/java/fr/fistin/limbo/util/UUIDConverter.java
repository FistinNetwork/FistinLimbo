package fr.fistin.limbo.util;

import java.util.UUID;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 16:18
 */
public class UUIDConverter {

    public static UUID fromString(final String input) {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

}
