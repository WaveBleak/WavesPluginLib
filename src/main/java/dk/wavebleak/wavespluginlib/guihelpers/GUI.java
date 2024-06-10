package dk.wavebleak.wavespluginlib.guihelpers;

import dk.wavebleak.wavespluginlib.WavesPluginLib;
import dk.wavebleak.wavespluginlib.inventoryutils.CloseInventoryData;
import dk.wavebleak.wavespluginlib.inventoryutils.CloseInventoryManager;
import dk.wavebleak.wavespluginlib.inventoryutils.InventoryData;
import dk.wavebleak.wavespluginlib.inventoryutils.InventoryManager;
import dk.wavebleak.wavespluginlib.itemhelpers.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


@SuppressWarnings("unused")
public abstract class GUI{
    protected String name = "Chest Inventory";
    protected int rows = 3;
    protected HashMap<Integer, ItemStack> slots = new HashMap<>();

    private boolean setRows = false;
    private ItemStack rowItem;
    private GUIBorderType borderType;

    protected abstract void init();
    protected abstract void onClick(InventoryClickEvent event);
    protected abstract void onClose(InventoryCloseEvent event);

    protected int getSlot(GUIAnchorHorizontal horizontal, GUIAnchorVertical vertical) {
        int col = 0;
        switch (horizontal) {
            case MIDDLE:
                col = 4;
                break;
            case RIGHT:
                col = 8;
                break;
        }

        int row = 0;
        switch (vertical) {
            case MIDDLE:
                row = rows / 2;  // floor division
                break;
            case BOTTOM:
                row = rows - 1;
                break;
        }

        return col + (row * 9);
    }

    protected void setRows(ItemStack item) {
        this.setRows = true;
        this.rowItem = item;
    }

    public GUI() {
        init();
    }

    protected void fillSlotsInRange(ItemStack item, int from, int to) {
        for (int i = from; i <= to; i++) {
            slots.put(i, item);
        }
    }

    protected void fillSlots(ItemStack item, int... slots) {
        for(int i : slots) {
            this.slots.put(i, item);
        }
    }

    protected void fillBorders(ItemStack item, GUIBorderType borderType) {
        this.setRows = true;
        this.rowItem = item;
        this.borderType = borderType;
    }

    protected static ItemStack getDefaultBorderItem() {
        return new ItemBuilder()
                .setMaterial(Material.STAINED_GLASS_PANE)
                .setData((byte)15)
                .setName("&c")
                .build();
    }

    public void open(Player... players) {
        Inventory inventory;

        inventory = Bukkit.createInventory(null, rows * 9, name);

        if(setRows) {
            int inventorySize = 9;

            if(borderType == GUIBorderType.ALL || borderType == GUIBorderType.RIGHT_LEFT) {
                IntStream.range(0, inventorySize).forEach(i -> {
                    inventory.setItem(i, rowItem);
                    inventory.setItem(inventorySize * (rows - 1) + i, rowItem);
                });
            }

            if(borderType == GUIBorderType.ALL || borderType == GUIBorderType.TOP_BOTTOM) {
                IntStream.range(1, rows).forEach(i -> {
                    int base = i * inventorySize;
                    inventory.setItem(base, rowItem);
                    inventory.setItem(base + inventorySize - 1, rowItem);
                });
            }
        }

        for(Map.Entry<Integer, ItemStack> entry : slots.entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();

            inventory.setItem(slot, item);
        }

        for(Player player : players) {
            player.openInventory(inventory);
        }

        InventoryManager inventoryManager = this::onClick;

        CloseInventoryManager closeInventoryManager = this::onClose;

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : players) {
                    WavesPluginLib.inventoryManager.put(player, new InventoryData(inventoryManager, inventory));
                    WavesPluginLib.closeInventoryManager.put(player, new CloseInventoryData(closeInventoryManager, inventory));
                }
            }
        }.runTaskLater(WavesPluginLib.pluginInstance, 1);
    }


}
