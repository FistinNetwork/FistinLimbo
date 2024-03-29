package fr.fistin.limbo.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class ConciseFormatter extends Formatter {

    private final Logger logger;
    private final boolean colored;

    public ConciseFormatter(Logger logger, boolean colored) {
        this.logger = logger;
        this.colored = colored;
    }

    @Override
    @SuppressWarnings("ThrowableResultIgnored")
    public String format(LogRecord record) {
        final StringBuilder formatted = new StringBuilder();

        formatted.append("[")
                .append(this.logger.getName())
                .append("] ")
                .append("[");

        this.appendLevel(formatted, record.getLevel());

        formatted.append("] ")
                .append(this.formatMessage(record))
                .append("\n");

        if (record.getThrown() != null) {
            final StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }

        return formatted.toString();
    }

    private void appendLevel(StringBuilder builder, Level level) {
        if (this.colored) {
            ChatColor color;

            if (level == Level.INFO) {
                color = ChatColor.AQUA;
            } else if (level == Level.WARNING) {
                color = ChatColor.YELLOW;
            } else if (level == Level.SEVERE) {
                color = ChatColor.RED;
            } else {
                color = ChatColor.BLUE;
            }

            builder.append(color).append(level.getLocalizedName()).append(ChatColor.RESET);
        } else {
            builder.append(level.getLocalizedName());
        }
    }
}
