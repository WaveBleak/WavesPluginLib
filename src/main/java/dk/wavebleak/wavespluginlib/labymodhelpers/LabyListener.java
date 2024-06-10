package dk.wavebleak.wavespluginlib.labymodhelpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class LabyListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("labymod3:main")) return;

        ByteBuf buf = Unpooled.wrappedBuffer(message);
        String key = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        String json = LabyModProtocol.readString(buf, Short.MAX_VALUE);

        if(!key.equals("input_prompt")) return;

        Gson gson = new Gson();
        JsonObject object = gson.fromJson(json, JsonObject.class);

        String id = object.get("id").getAsString();
        String value = object.get("value").getAsString();

        InputBoxResponseEvent event = new InputBoxResponseEvent(player, id, value);

        Bukkit.getPluginManager().callEvent(event);
    }
}
