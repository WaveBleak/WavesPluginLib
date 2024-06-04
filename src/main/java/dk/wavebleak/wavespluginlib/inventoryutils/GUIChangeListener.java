package dk.wavebleak.wavespluginlib.inventoryutils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static dk.wavebleak.wavespluginlib.WavesPluginLib.closeInventoryManager;
import static dk.wavebleak.wavespluginlib.WavesPluginLib.inventoryManager;


public class GUIChangeListener implements Listener {


    @EventHandler
    public void guiCloseEvent(InventoryCloseEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
        inventoryManager.remove(player);

        if(!closeInventoryManager.containsKey(player)) {
            return;
        }
        if(!closeInventoryManager.get(player).getInventory().equals(event.getInventory())) {
            return;
        }

        closeInventoryManager.get(player).getLambda().run(event);
        closeInventoryManager.remove(player);
    }


    @EventHandler
    public void guiPickEvent(InventoryClickEvent event) {
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());

        if(!inventoryManager.containsKey(player)) {
            return;
        }
        if(!inventoryManager.get(player).getInventory().equals(event.getInventory())) {
            return;
        }
        

        event.setCancelled(true);
        inventoryManager.get(player).getLambda().run(event);
    }

}
