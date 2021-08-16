package fr.fistin.limbo.network.protocol;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 22:43
 */
public enum ProtocolState {

    HANDSHAKE(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3),

    ;

    private final int id;

    ProtocolState(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ProtocolState getStateById(int id) {
        for (ProtocolState state : values()) {
            if (state.getId() == id) {
                return state;
            }
        }
        return null;
    }

}
