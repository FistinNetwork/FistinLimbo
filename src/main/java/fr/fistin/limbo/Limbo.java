package fr.fistin.limbo;

import fr.fistin.limbo.command.LimboCommandManager;
import fr.fistin.limbo.network.NetworkManager;
import fr.fistin.limbo.network.protocol.encryption.EncryptionUtil;
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
import java.security.KeyPair;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 13:30
 */
public class Limbo {

    /** Encryption */
    private byte[] verifyToken = new byte[4];
    private KeyPair keyPair;

    /** Command */
    private LimboCommandManager commandManager;

    /** Network */
    private NetworkManager networkManager;

    /** Logger */
    private ConsoleReader consoleReader;
    private static LimboLogger logger;

    /** World */
    private World world;

    /** Limbo Global Information */
    private boolean running;

    /** Configuration */
    private final LimboConfiguration configuration;

    protected Limbo(LimboConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        LimboLogger.printHeaderMessage();

        this.setupLogger();

        System.out.println("Starting FistinLimbo...");

        this.loadWorld(configuration.getSchematicFile());

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
            ex.printStackTrace();

            System.out.println("Couldn't load world: loading failed!");
        }
    }

    private void run() {
        this.running = true;
        this.networkManager = new NetworkManager(this);
        this.commandManager = new LimboCommandManager(this);

        System.out.println("Generating keypair...");
        this.keyPair = EncryptionUtil.generateKeyPair();

        new Random().nextBytes(verifyToken);

        this.commandManager.start();
        this.networkManager.start();
    }

    public void stop() {
        this.running = false;
        this.networkManager.shutdown();
        this.commandManager.shutdown();

        System.out.println("FistinLimbo is now down, see you soon!");

        this.waitLogger();
    }

    private void waitLogger() {
        try {
            if (!logger.getDispatcher().getQueue().isEmpty()) {
                Thread.sleep(500);
                this.waitLogger();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LimboConfiguration getConfiguration() {
        return this.configuration;
    }

    public boolean isRunning() {
        return this.running;
    }

    public World getWorld() {
        return this.world;
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public LimboCommandManager getCommandManager() {
        return this.commandManager;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public static Logger getLogger() {
        return logger;
    }

}
