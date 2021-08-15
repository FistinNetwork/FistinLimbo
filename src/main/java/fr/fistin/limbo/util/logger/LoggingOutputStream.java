package fr.fistin.limbo.util.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class LoggingOutputStream extends ByteArrayOutputStream {

    private static final String separator = System.getProperty( "line.separator" );

    private final Logger logger;
    private final Level level;

    public LoggingOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void flush() throws IOException
    {
        final String contents = this.toString(StandardCharsets.UTF_8.name());
        super.reset();
        if (!contents.isEmpty() && !contents.equals(separator)) {
            this.logger.logp(level, "", "", contents);
        }
    }

}
