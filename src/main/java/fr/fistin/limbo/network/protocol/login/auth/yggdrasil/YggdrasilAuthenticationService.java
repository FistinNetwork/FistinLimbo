package fr.fistin.limbo.network.protocol.login.auth.yggdrasil;

import com.google.gson.*;
import fr.fistin.limbo.network.protocol.login.auth.HttpAuthenticationService;
import fr.fistin.limbo.network.protocol.login.auth.exception.AuthenticationException;
import fr.fistin.limbo.network.protocol.login.auth.exception.AuthenticationUnavailableException;
import fr.fistin.limbo.network.protocol.login.auth.exception.InvalidCredentialsException;
import fr.fistin.limbo.network.protocol.login.auth.exception.UserMigratedException;
import fr.fistin.limbo.network.protocol.login.auth.profile.GameProfile;
import fr.fistin.limbo.network.protocol.login.auth.profile.property.PropertyMap;
import fr.fistin.limbo.network.protocol.login.auth.yggdrasil.response.YggdrasilResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.UUID;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 19:42
 */
public class YggdrasilAuthenticationService extends HttpAuthenticationService {

    private final Gson gson;

    private final String clientToken;

    public YggdrasilAuthenticationService(String clientToken) {
        this.clientToken = clientToken;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(GameProfile.class, new GameProfileSerializer())
                .registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer())
                .create();
    }

    public String getClientToken() {
        return this.clientToken;
    }

    public YggdrasilMinecraftSessionService createMinecraftSessionService() {
        return new YggdrasilMinecraftSessionService(this);
    }

    protected <T extends YggdrasilResponse> T makeRequest(URL url, Object input, Class<T> classOfT) throws AuthenticationException {
        try {
            final String jsonResult = input == null ? this.sendGetRequest(url) : this.sendPostRequest(url, this.gson.toJson(input), "application/json");
            final T result = this.gson.fromJson(jsonResult, classOfT);

            if (result == null) {
                return null;
            } else if (result.getError() != null && result.getError().isEmpty()) {
                if ("UserMigratedException".equals(result.getCause())) {
                    throw new UserMigratedException(result.getErrorMessage());
                } else if (result.getError().equals("ForbiddenOperationException")) {
                    throw new InvalidCredentialsException(result.getErrorMessage());
                } else {
                    throw new AuthenticationException(result.getErrorMessage());
                }
            } else {
                return result;
            }
        } catch (IOException | IllegalStateException | JsonParseException e) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server", e);
        }
    }

    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

        public GameProfileSerializer() {}

        public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject object = (JsonObject) json;
            final UUID id = object.has("id") ? (UUID)context.deserialize(object.get("id"), UUID.class) : null;
            final String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;

            return new GameProfile(id, name);
        }

        public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
            final JsonObject result = new JsonObject();

            if (src.getId() != null) {
                result.add("id", context.serialize(src.getId()));
            }

            if (src.getName() != null) {
                result.addProperty("name", src.getName());
            }

            return result;
        }
    }

}
