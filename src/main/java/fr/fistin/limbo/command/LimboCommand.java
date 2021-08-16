package fr.fistin.limbo.command;

public abstract class LimboCommand {

    protected String commandLabel;

    public LimboCommand(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    public abstract boolean execute(String[] args);

    public String getCommandLabel() {
        return this.commandLabel;
    }

    public abstract String getHelp();

}
