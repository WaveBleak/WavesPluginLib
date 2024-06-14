package dk.wavebleak.wavespluginlib.anvilguihelper;

import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
public interface AnvilContainerWrapper {

    default String getRenameText() {
        return null;
    }

    default void setRenameText(String text) {}

    Inventory getBukkitInventory();
}
