package fr.fistin.limbo.network.protocol.login.auth.yggdrasil.response;

import fr.fistin.limbo.network.protocol.login.auth.profile.property.PropertyMap;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 21:49
 */
public class YggdrasilMinecraftServerHasJoinedResponse extends YggdrasilResponse {

    private String id;
    private PropertyMap properties;

    public YggdrasilMinecraftServerHasJoinedResponse() {}

    public String getId() {
        return this.id;
    }

    public PropertyMap getProperties() {
        return this.properties;
    }

}
