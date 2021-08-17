package fr.fistin.limbo.player.settings;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 22:43
 */
public enum ChatMode {

    ENABLED(0),
    COMMANDS_ONLY(1),
    HIDDEN(2);

    private final int id;

    ChatMode(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ChatMode getModeById(int id) {
        for (ChatMode mode : values()) {
            if (id == mode.getId()) {
                return mode;
            }
        }
        return null;
    }

}
