package fr.fistin.limbo.command.model;

import fr.fistin.limbo.Limbo;
import fr.fistin.limbo.command.LimboCommand;
import fr.fistin.limbo.command.LimboCommandManager;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 22:33
 */
public class StopCommand extends LimboCommand {

    public StopCommand() {
        super("stop");
    }

    @Override
    public boolean execute(String[] args) {
        System.exit(0);
        return true;
    }

    @Override
    public String getHelp() {
        return "Stop the Limbo Minecraft server";
    }

}
