package dk.wavebleak.wavespluginlib.labymodhelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

import java.util.UUID;
@SuppressWarnings("unused")
public class LabyModUtils {

    public static void updateBalanceDisplay(Player player, EnumBalanceType type, boolean visible, int balance ) {
        JsonObject economyObject = new JsonObject();
        JsonObject cashObject = new JsonObject();

        cashObject.addProperty( "visible", visible );
        cashObject.addProperty( "balance", balance );
        economyObject.add(type.getKey(), cashObject);

        LabyModProtocol.sendLabyModMessage( player, "economy", economyObject );
    }
    public static void setSubtitle(Player receiver, UUID subtitlePlayer, String value, float size) {
        if(size > 1.6f || size < 0.8f) throw new IllegalStateException("Range has to be between 0.8 and 1.6");
        JsonArray array = new JsonArray();

        JsonObject subtitle = new JsonObject();

        subtitle.addProperty( "uuid", subtitlePlayer.toString() );
        subtitle.addProperty( "size", 0.8d );

        if(value != null) subtitle.addProperty( "value", value );

        array.add(subtitle);

        LabyModProtocol.sendLabyModMessage( receiver, "account_subtitle", array );
    }

    public static void sendServerBanner(Player player, String imageUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("url", imageUrl); // Url of the image
        LabyModProtocol.sendLabyModMessage(player, "server_banner", object);
    }


    public static void setMiddleClickActions(Player player, LabyAction... actions) {
        JsonArray array = new JsonArray();

        for(LabyAction action : actions) {
            JsonObject entry = new JsonObject();
            entry.addProperty("displayName", action.getDisplayName());
            entry.addProperty("type", action.getType().name());
            entry.addProperty("value", action.getValue());
            array.add(entry);
        }

        LabyModProtocol.sendLabyModMessage(player, "user_menu_actions", array);
    }

    public static void forceEmote(Player receiver, UUID npcUUID, EnumEmoteType typeOfEmote) {
        JsonArray array = new JsonArray();

        JsonObject forcedEmote = new JsonObject();
        forcedEmote.addProperty("uuid", npcUUID.toString());
        forcedEmote.addProperty("emote_id", typeOfEmote.getId());
        array.add(forcedEmote);

        LabyModProtocol.sendLabyModMessage(receiver, "emote_api", array);
    }

    public static void sendCurrentPlayingGamemode(Player player, boolean visible, String gamemodeName ) {
        JsonObject object = new JsonObject();
        object.addProperty( "show_gamemode", visible );
        object.addProperty( "gamemode_name", gamemodeName );

        // Send to LabyMod using the API
        LabyModProtocol.sendLabyModMessage( player, "server_gamemode", object );
    }

    public static void sendInputPrompt(Player player, int promptSessionId, String title, String placeholder, String value, int maxLength ) {
        JsonObject object = new JsonObject();
        object.addProperty( "id", promptSessionId );
        object.addProperty( "message", title );
        object.addProperty( "max_length", maxLength );

        String finalValue = "";
        if(!(value == null || value.isEmpty())) {
            finalValue = value;
        }
        object.addProperty("value", finalValue);

        String finalPlaceholder = "";
        if(!(placeholder == null || placeholder.isEmpty())) {
            finalPlaceholder = placeholder;
        }
        object.addProperty("placeholder", finalPlaceholder);

        LabyModProtocol.sendLabyModMessage( player, "input_prompt", object );

        // If you want to use the new text format in 1.16+
        // object.add("raw_json_text", textObject );

    }

    public static void sendBuggedInputPrompt(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty( "id", 234 );
        object.addProperty( "message", "HEJ" );
        object.addProperty( "max_length", 50 );

        LabyModProtocol.sendLabyModMessage( player, "input_prompt", object );

        // If you want to use the new text format in 1.16+
        // object.add("raw_json_text", textObject );

    }






}
