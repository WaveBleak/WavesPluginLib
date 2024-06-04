package dk.wavebleak.wavespluginlib.inventoryutils;

import org.bukkit.event.inventory.InventoryCloseEvent;

@SuppressWarnings("unused")
public interface CloseInventoryManager {
    void run(InventoryCloseEvent event);
}
