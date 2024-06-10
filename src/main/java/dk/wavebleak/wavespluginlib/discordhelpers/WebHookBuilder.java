package dk.wavebleak.wavespluginlib.discordhelpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dk.wavebleak.wavespluginlib.WavesPluginLib;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@SuppressWarnings("unused")
public class WebHookBuilder {
    private final String webhookURL;
    private String message = null;
    private String username = null;
    private String avatarURL = null;
    private final List<Embed> embeds = new ArrayList<>();
    private CompletableFuture<Integer> handleResponse = null;

    public WebHookBuilder(String webhookURL) {
        this.webhookURL = webhookURL;
    }

    public WebHookBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public WebHookBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public WebHookBuilder setAvatar(String avatarURL) {
        this.avatarURL = avatarURL;
        return this;
    }

    public WebHookBuilder addEmbed(Embed embed) {
        this.embeds.add(embed);
        return this;
    }

    public WebHookBuilder handleResponse(CompletableFuture<Integer> response) {
        this.handleResponse = response;
        return this;
    }


    public void send() {
        JsonObject object = new JsonObject();
        object.addProperty("tts", false);

        JsonArray embedArray = new JsonArray();
        for (Embed embed : embeds) {
            embedArray.add(embed.serialize());
        }
        object.add("embeds", embedArray);
        if(message != null) {
            object.addProperty("content", message);
        }
        if(username != null) {
            object.addProperty("username", username);
        }
        if(avatarURL != null) {
            object.addProperty("avatar_url", avatarURL);
        }

        WavesPluginLib.pluginInstance.getLogger().info(object.toString());

        try {
            URL url = new URL(webhookURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(object.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            if (handleResponse != null) {
                handleResponse.complete(responseCode);
            }

        }catch (IOException e) {
            if(handleResponse != null) {
                handleResponse.completeExceptionally(e);
            }
        }



    }


}
