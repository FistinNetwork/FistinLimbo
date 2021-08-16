package fr.fistin.limbo.player;

import java.util.UUID;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 15/08/2021 at 18:51
 */
public class GameProfile {

    private UUID uuid;
    private String username;

    public GameProfile() {
        this.uuid = UUID.randomUUID();
        this.username = "";
    }

    public GameProfile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
