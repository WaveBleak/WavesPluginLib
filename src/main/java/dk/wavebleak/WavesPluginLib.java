package dk.wavebleak;

import org.bukkit.plugin.java.JavaPlugin;

public class WavesPluginLib {
    public static JavaPlugin instance;
    public WavesPluginLib(JavaPlugin plugin) {
        instance = plugin;
        //TODO: Add json manager and other stuff
    }

}
