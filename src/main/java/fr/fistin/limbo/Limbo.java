package fr.fistin.limbo;

import fr.fistin.limbo.util.References;
import fr.fistin.limbo.util.logger.LimboLogger;
import fr.fistin.limbo.util.logger.LoggingOutputStream;
import fr.fistin.limbo.world.Schematic;
import fr.fistin.limbo.world.World;
import fr.fistin.limbo.world.nbt.NBTTag;
import jline.console.ConsoleReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class Limbo {

    /** Logger */
    private ConsoleReader consoleReader;
    private static LimboLogger logger;

    /** Configuration */
    private final LimboConfiguration limboConfiguration;

    /** Network */
    private NetworkManager networkManager;

    /** World */
    private World world;

    /** Limbo Global Information */
    private boolean running;

    protected Limbo(LimboConfiguration limboConfiguration) {
        this.limboConfiguration = limboConfiguration;
    }

    public void start() {
        this.setupLogger();

        this.loadWorld(limboConfiguration.getSchematicFile());

        if (this.world != null) {
            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

            this.run();

            System.exit(0);
        }
    }

    private void setupLogger() {
        this.setupConsoleReader();

        if (!new File("logs/").mkdirs()) {
            logger = new LimboLogger(this, References.NAME, "logs/limbo.log");

            System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
        }
    }

    private void setupConsoleReader() {
        try {
            this.consoleReader = new ConsoleReader();
            this.consoleReader.setExpandEvents(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWorld(String fileName) {
        System.out.println("Loading schematic from '" + fileName + "'...");

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            this.world = Schematic.toWorld(NBTTag.readTag(new GZIPInputStream(fileInputStream)).toCompoundTag());

            System.out.println("World successfully loaded!");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

            System.out.println("Couldn't load world: loading failed!");
        }
    }

    private void run() {
        this.networkManager = new NetworkManager(this.world, this.limboConfiguration);
        this.running = true;

        try {
            this.networkManager.bind(this.limboConfiguration.getIp(), this.limboConfiguration.getPort());

            logger.info("Listening on port " + this.limboConfiguration.getPort());

            while (this.running) {
                this.networkManager.select();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void stop() {
        this.running = false;
        this.networkManager.stop();

        this.waitLogger();
    }

    private void waitLogger() {
        try {
            if (logger.getDispatcher().getQueue().isEmpty()) {
                System.exit(0);
            }
            else {
                Thread.sleep(500);
                this.waitLogger();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public static Logger getLogger() {
        return logger;
    }

}
