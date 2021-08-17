package fr.fistin.limbo.network.protocol.login.auth.profile;

import com.google.gson.*;
import fr.fistin.limbo.network.protocol.login.auth.profile.property.Property;
import fr.fistin.limbo.network.protocol.login.auth.profile.property.PropertyMap;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 19:27
 */
public class GameProfile {

    private final UUID id;
    private final String name;
    private final PropertyMap properties = new PropertyMap();

    public GameProfile(UUID id, String name) {
        if (id == null && (!name.isEmpty() && name == null)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        } else {
            this.id = id;
            this.name = name;
        }
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public PropertyMap getProperties() {
        return this.properties;
    }

    public boolean isComplete() {
        return this.id != null && (!name.isEmpty() && name != null);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            final GameProfile that = (GameProfile) o;

            if (this.id != null) {
                if (!this.id.equals(that.id)) {
                    return false;
                }
            } else if (that.id != null) {
                return false;
            }

            if (this.name != null) {
                return this.name.equals(that.name);
            } else {
                return that.name == null;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;

        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);

        return result;
    }

}
