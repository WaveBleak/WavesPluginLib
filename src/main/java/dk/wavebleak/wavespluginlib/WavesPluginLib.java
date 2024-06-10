package dk.wavebleak.wavespluginlib;

import dk.wavebleak.wavespluginlib.inventoryutils.CloseInventoryData;
import dk.wavebleak.wavespluginlib.inventoryutils.GUIChangeListener;
import dk.wavebleak.wavespluginlib.inventoryutils.InventoryData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@SuppressWarnings("unused")
public class WavesPluginLib {
    public static JavaPlugin pluginInstance;
    public static HashMap<Player, InventoryData> inventoryManager = new HashMap<>();
    public static HashMap<Player, CloseInventoryData> closeInventoryManager = new HashMap<>();
    @SuppressWarnings("unused")
    public static void init(JavaPlugin plugin) {
        pluginInstance = plugin;
        Bukkit.getPluginManager().registerEvents(new GUIChangeListener(), pluginInstance);
    }


}
