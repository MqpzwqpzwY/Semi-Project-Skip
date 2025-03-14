package service;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;

public class TimestampAdapter implements JsonDeserializer<Timestamp>, JsonSerializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return new Timestamp(json.getAsLong()); // 밀리초 단위 변환
        } catch (Exception e) {
            throw new JsonParseException("Invalid timestamp format: " + json.getAsString());
        }
    }

    @Override
    public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime()); // Timestamp → 밀리초
    }
}
