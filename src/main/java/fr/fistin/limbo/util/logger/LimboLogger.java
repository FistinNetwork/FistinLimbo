package fr.fistin.limbo.util.logger;

import fr.fistin.limbo.Limbo;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class LimboLogger extends Logger {

    private final LogDispatcher dispatcher = new LogDispatcher(this);

    @SuppressWarnings( { "CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction" } )
    public LimboLogger(Limbo limbo, String name, String filePattern) {
        super(name, null);
        this.setLevel(Level.ALL);

        try {
            final FileHandler fileHandler = new FileHandler(filePattern);
            fileHandler.setFormatter(new ConciseFormatter(this, false));

            this.addHandler(fileHandler);

            final ColouredWriter consoleHandler = new ColouredWriter(limbo.getConsoleReader());
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new ConciseFormatter(this, true));

            this.addHandler(consoleHandler);
        } catch (IOException e) {
            System.err.println("Couldn't register logger !");

            limbo.stop();
        }

        this.dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        this.dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }

    public static void printHeaderMessage() {
        final String text =
                        "$$$$$$$$\\ $$\\             $$\\     $$\\           $$\\       $$\\               $$\\                 \n" +
                        "$$  _____|\\__|            $$ |    \\__|          $$ |      \\__|              $$ |                \n" +
                        "$$ |      $$\\  $$$$$$$\\ $$$$$$\\   $$\\ $$$$$$$\\  $$ |      $$\\ $$$$$$\\$$$$\\  $$$$$$$\\   $$$$$$\\  \n" +
                        "$$$$$\\    $$ |$$  _____|\\_$$  _|  $$ |$$  __$$\\ $$ |      $$ |$$  _$$  _$$\\ $$  __$$\\ $$  __$$\\ \n" +
                        "$$  __|   $$ |\\$$$$$$\\    $$ |    $$ |$$ |  $$ |$$ |      $$ |$$ / $$ / $$ |$$ |  $$ |$$ /  $$ |\n" +
                        "$$ |      $$ | \\____$$\\   $$ |$$\\ $$ |$$ |  $$ |$$ |      $$ |$$ | $$ | $$ |$$ |  $$ |$$ |  $$ |\n" +
                        "$$ |      $$ |$$$$$$$  |  \\$$$$  |$$ |$$ |  $$ |$$$$$$$$\\ $$ |$$ | $$ | $$ |$$$$$$$  |\\$$$$$$  |\n" +
                        "\\__|      \\__|\\_______/    \\____/ \\__|\\__|  \\__|\\________|\\__|\\__| \\__| \\__|\\_______/  \\______/ ";

        System.out.println(text.replaceAll("\\$", "█"));
    }

    public LogDispatcher getDispatcher() {
        return this.dispatcher;
    }

}
