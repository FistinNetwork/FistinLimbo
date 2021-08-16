package fr.fistin.limbo.command;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.command.model.HelpCommand;
import fr.fistin.limbo.command.model.StopCommand;
import fr.fistin.limbo.util.logger.ChatColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class LimboCommandManager {

    private Thread commandThread;

    private final List<LimboCommand> commands;

    private final Limbo limbo;

    public LimboCommandManager(Limbo limbo) {
        this.limbo = limbo;
        this.commands = new ArrayList<>();

        this.addCommand(new HelpCommand(this));
        this.addCommand(new StopCommand());
    }

    public void start() {
        System.out.println("Starting Limbo command manager...");

        this.commandThread = new Thread(() -> {
            while (this.limbo.isRunning()) {
                String line = null;

                try {
                    line = this.limbo.getConsoleReader().readLine(">");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (line != null) {
                    this.inputCommand(line);
                }
            }
        });
        this.commandThread.start();
    }

    public void shutdown() {
        System.out.println("Stopping Limbo command manager...");

        this.commandThread.interrupt();
    }

    public void inputCommand(String data) {
        String[] args = data.split(" ");
        final String commandLabel = args[0];

        args = Arrays.copyOfRange(args, 1, args.length);

        for (LimboCommand command : this.commands) {
            if (command.getCommandLabel().equalsIgnoreCase(commandLabel)) {
                if (!command.execute(args)) {
                    Limbo.getLogger().log(Level.SEVERE, String.format("An error occurred while executing %s command !", command.getCommandLabel()));
                }
                return;
            }
        }

        System.out.println("Command '" + commandLabel + "' doesn't exist !");
    }

    public void showHelp() {
        Limbo.getLogger().log(Level.INFO, ChatColor.WHITE + "-------------" + ChatColor.AQUA + " FistinLimbo Help" + ChatColor.WHITE + " -------------");

        for (LimboCommand command : this.commands) {
            final String help = command.getHelp();

            if (help != null) {
                Limbo.getLogger().log(Level.INFO, String.format("%s%s: %s", ChatColor.AQUA + command.getCommandLabel(), ChatColor.WHITE, help));
            } else {
                Limbo.getLogger().log(Level.INFO, String.format("%s: %s", command.getCommandLabel(), "this command doesn't have help text"));
            }
        }

        Limbo.getLogger().log(Level.INFO, ChatColor.WHITE + "--------------------------------------------");
    }

    public void addCommand(LimboCommand command) {
        this.commands.add(command);

        System.out.println("Registered '" + command.getCommandLabel() + "' command");
    }

    public void removeCommand(LimboCommand command) {
        this.commands.remove(command);
    }

    public List<LimboCommand> getCommands() {
        return this.commands;
    }

}
