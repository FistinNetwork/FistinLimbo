package fr.fistin.limbo.network.packet;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 15:13
 */
public abstract class Packet {

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
