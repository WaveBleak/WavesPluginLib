package dk.wavebleak.wavespluginlib.inventoryutils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;

@Getter
@AllArgsConstructor
@SuppressWarnings("unused")
public class InventoryData {
    private final InventoryManager lambda;
    private final Inventory inventory;

}