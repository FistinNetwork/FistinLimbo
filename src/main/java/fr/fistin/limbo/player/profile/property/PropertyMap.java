package fr.fistin.limbo.player.profile.property;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Project: FistinLimbo
 * Created by AstFaster
 * on 17/08/2021 at 22:15
 */
public class PropertyMap extends ForwardingMultimap<String, Property> {

    private final Multimap<String, Property> properties = LinkedHashMultimap.create();

    public PropertyMap() {}

    protected Multimap<String, Property> delegate() {
        return this.properties;
    }

    public static class Serializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

        public Serializer() {}

        public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final PropertyMap result = new PropertyMap();
            if (json instanceof JsonObject) {
                final JsonObject object = (JsonObject) json;
                final Iterator<Map.Entry<String, JsonElement>> entryIterator = object.entrySet().iterator();

                while(true) {
                    Map.Entry<String, JsonElement> entry;
                    do {
                        if (!entryIterator.hasNext()) {
                            return result;
                        }

                        entry = entryIterator.next();
                    } while(!(entry.getValue() instanceof JsonArray));

                    for (JsonElement element : (JsonArray) entry.getValue()) {
                        result.put(entry.getKey(), new Property(entry.getKey(), element.getAsString()));
                    }
                }
            } else if (json instanceof JsonArray) {

                for (JsonElement element : (JsonArray) json) {
                    if (element instanceof JsonObject) {
                        final JsonObject object = (JsonObject) element;
                        final String name = object.getAsJsonPrimitive("name").getAsString();
                        final String value = object.getAsJsonPrimitive("value").getAsString();

                        if (object.has("signature")) {
                            result.put(name, new Property(name, value, object.getAsJsonPrimitive("signature").getAsString()));
                        } else {
                            result.put(name, new Property(name, value));
                        }
                    }
                }
            }

            return result;
        }

        public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray result = new JsonArray();

            JsonObject object;
            for(Iterator<Property> valueIterator = src.values().iterator(); valueIterator.hasNext(); result.add(object)) {
                final Property property = valueIterator.next();

                object = new JsonObject();
                object.addProperty("name", property.getName());
                object.addProperty("value", property.getValue());

                if (property.hasSignature()) {
                    object.addProperty("signature", property.getSignature());
                }
            }

            return result;
        }
    }
}
