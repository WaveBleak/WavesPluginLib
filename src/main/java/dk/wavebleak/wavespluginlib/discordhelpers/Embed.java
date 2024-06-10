package dk.wavebleak.wavespluginlib.discordhelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

import java.util.List;
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class Embed {
    private final String title;
    private final String description;
    private final String url;
    private final Integer color;
    private final String footer;
    private final String image;
    private final String thumbnail;
    private final List<Field> fields;


    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        if(title != null) {
            object.addProperty("title", title);
        }
        if(description != null) {
            object.addProperty("description", description);
        }
        if(url != null) {
            object.addProperty("url", url);
        }
        if(color != null) {
            object.addProperty("color", color);
        }
        if(footer != null) {
            object.addProperty("footer", footer);
        }
        if(image != null) {
            JsonObject imageObject = new JsonObject();
            imageObject.addProperty("url", image);
            object.add("image", imageObject);
        }
        if(thumbnail != null) {
            object.addProperty("thumbnail", thumbnail);
        }
        if (fields != null) {
            JsonArray fieldsArray = new JsonArray();
            for (Field field : fields) {
                JsonObject fieldObject = new JsonObject();
                fieldObject.addProperty("name", field.getName());
                fieldObject.addProperty("value", field.getValue());
                fieldObject.addProperty("inline", field.isInline());
                fieldsArray.add(fieldObject);
            }
            object.add("fields", fieldsArray);
        }
        return object;
    }

}
