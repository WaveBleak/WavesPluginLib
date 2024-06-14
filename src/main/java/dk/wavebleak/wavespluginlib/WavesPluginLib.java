package dk.wavebleak.wavespluginlib;

import dk.wavebleak.wavespluginlib.inventoryutils.CloseInventoryData;
import dk.wavebleak.wavespluginlib.inventoryutils.GUIChangeListener;
import dk.wavebleak.wavespluginlib.inventoryutils.InventoryData;
import dk.wavebleak.wavespluginlib.labymodhelpers.LabyListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.concurrent.Executor;

@SuppressWarnings("unused")
public class WavesPluginLib {
    public static JavaPlugin pluginInstance;
    public static HashMap<Player, InventoryData> inventoryManager = new HashMap<>();
    public static HashMap<Player, CloseInventoryData> closeInventoryManager = new HashMap<>();
    public static Executor mainThreadExecutor;
    @SuppressWarnings("unused")
    public static void init(JavaPlugin plugin) {
        pluginInstance = plugin;
        pluginInstance.getServer().getMessenger().registerIncomingPluginChannel(plugin, "labymod3:main", new LabyListener());

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        mainThreadExecutor = command -> Bukkit.getServer().getScheduler().runTask(plugin, command);

        Bukkit.getPluginManager().registerEvents(new GUIChangeListener(), pluginInstance);

    }





}
