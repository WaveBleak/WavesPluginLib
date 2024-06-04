package dk.wavebleak.wavespluginlib.inventoryutils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
@Getter
@AllArgsConstructor
public class CloseInventoryData {
    private final CloseInventoryManager lambda;
    private final Inventory inventory;
}
