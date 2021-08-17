package fr.fistin.limbo.network.protocol.login.auth.yggdrasil;

import fr.fistin.limbo.network.protocol.login.auth.HttpAuthenticationService;
import fr.fistin.limbo.network.protocol.login.auth.exception.AuthenticationException;
import fr.fistin.limbo.network.protocol.login.auth.exception.AuthenticationUnavailableException;
import fr.fistin.limbo.network.protocol.login.auth.profile.GameProfile;
import fr.fistin.limbo.network.protocol.login.auth.yggdrasil.response.YggdrasilMinecraftServerHasJoinedResponse;
import fr.fistin.limbo.util.UUIDUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 20:08
 */
public class YggdrasilMinecraftSessionService {

    private static final URL CHECK_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/hasJoined");

    private final YggdrasilAuthenticationService authenticationService;

    public YggdrasilMinecraftSessionService(YggdrasilAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public GameProfile hasJoinedServer(GameProfile profile, String serverId) throws AuthenticationUnavailableException {
        final Map<String, Object> arguments = new HashMap<>();

        arguments.put("username", profile.getName());
        arguments.put("serverId", serverId);

        final URL url = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(arguments));

        try {
            final YggdrasilMinecraftServerHasJoinedResponse response = this.authenticationService.makeRequest(url, null, YggdrasilMinecraftServerHasJoinedResponse.class);

            if (response != null && response.getId() != null) {
                final GameProfile result = new GameProfile(UUIDUtil.fromString(response.getId()), profile.getName());

                if (response.getProperties() != null) {
                    result.getProperties().putAll(response.getProperties());
                }

                return result;
            }
        } catch (AuthenticationUnavailableException e) {
            e.printStackTrace();
            throw e;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
