package fr.fistin.limbo.chat;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 22:15
 */
public enum ChatPosition {

    CHAT(0),
    SYSTEM(1),
    ACTION_BAR(2);

    private final int id;

    ChatPosition(int id){
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
