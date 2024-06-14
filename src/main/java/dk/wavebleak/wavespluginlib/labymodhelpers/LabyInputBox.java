package dk.wavebleak.wavespluginlib.labymodhelpers;

import dk.wavebleak.wavespluginlib.WavesPluginLib;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class LabyInputBox {
    private final int session;
    private String title = "Input Box";
    private int maxLength = 50;
    private String placeholder = null;
    private String value = null;
    private Consumer<String> whenFinished = null;

    public LabyInputBox() {
        Random random = new Random();
        session = random.nextInt(Integer.MAX_VALUE);
    }

    public LabyInputBox setTitle(String title) {
        this.title = title;
        return this;
    }

    public LabyInputBox setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public LabyInputBox setValue(String value) {
        this.value = value;
        return this;

    }
    public LabyInputBox setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public LabyInputBox onFinish(Consumer<String> whenFinished) {
        this.whenFinished = whenFinished;
        return this;
    }


    public void open(Player player) {
        LabyModUtils.sendInputPrompt(player, session, title, placeholder, value, maxLength);
        WavesPluginLib.inputBoxSessions.put(String.valueOf(session), whenFinished);
    }



}
