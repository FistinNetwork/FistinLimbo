package fr.fistin.limbo.player.settings;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 16/08/2021 at 22:54
 */
public enum MainHand {

    LEFT(0),
    RIGHT(1);

    private final int id;

    MainHand(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MainHand getMainHandById(int id) {
        for (MainHand hand : values()) {
            if (id == hand.getId()) {
                return hand;
            }
        }
        return null;
    }

}
