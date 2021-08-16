package fr.fistin.limbo.command.model;

import fr.fistin.limbo.command.LimboCommand;
import fr.fistin.limbo.command.LimboCommandManager;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 22:33
 */
public class HelpCommand extends LimboCommand {

    private final LimboCommandManager commandManager;

    public HelpCommand(LimboCommandManager commandManager) {
        super("help");
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(String[] args) {
        this.commandManager.showHelp();
        return true;
    }

    @Override
    public String getHelp() {
        return "Show the help of all registered commands";
    }

}
