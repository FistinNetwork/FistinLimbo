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

    public void printHeaderMessage() {
        this.log(Level.INFO,"########################################");
        this.log(Level.INFO,"#####          Welcome in          #####");
        this.log(Level.INFO,"#####    Hydra - Fistin Network    #####");
        this.log(Level.INFO,"##### Authors: Faustin - AstFaster #####");
        this.log(Level.INFO,"########################################");
    }

    public void printFooterMessage() {
        this.log(Level.INFO,"########################################");
        this.log(Level.INFO,"#####           Stopping           #####");
        this.log(Level.INFO,"#####    Hydra - Fistin Network    #####");
        this.log(Level.INFO,"##### Authors: Faustin - AstFaster #####");
        this.log(Level.INFO,"########################################");
    }

    public LogDispatcher getDispatcher() {
        return this.dispatcher;
    }

}
