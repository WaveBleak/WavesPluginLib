package dk.wavebleak.wavespluginlib.guihelpers;

import dk.wavebleak.wavespluginlib.WavesPluginLib;
import dk.wavebleak.wavespluginlib.inventoryutils.CloseInventoryData;
import dk.wavebleak.wavespluginlib.inventoryutils.CloseInventoryManager;
import dk.wavebleak.wavespluginlib.inventoryutils.InventoryData;
import dk.wavebleak.wavespluginlib.inventoryutils.InventoryManager;
import dk.wavebleak.wavespluginlib.itemhelpers.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class GuiBuilder {

    private final HashMap<Integer, ItemStack> slotsToFill = new HashMap<>();
    private final HashMap<Player, InventoryManager> whenClicked = new HashMap<>();
    private final HashMap<Player, CloseInventoryManager> whenClosed = new HashMap<>();
    private String name = null;
    private int rows = 3;
    private boolean setRows = false;
    private ItemStack rowItem;
    private GUIBorderType borderType;
    public static ItemStack getDefaultBorderItem() {
        return new ItemBuilder()
                .setMaterial(Material.STAINED_GLASS_PANE)
                .setData((byte)15)
                .setName("&c")
                .build();
    }

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

    public GuiBuilder whenClicked(Player player, InventoryManager whenClicked) {
        this.whenClicked.put(player, whenClicked);
        return this;
    }

    public GuiBuilder whenClosed(Player player, CloseInventoryManager whenClosed) {
        this.whenClosed.put(player, whenClosed);
        return this;
    }

    public GuiBuilder fillBorders(ItemStack item, GUIBorderType borderType) {
        this.setRows = true;
        this.rowItem = item;
        this.borderType = borderType;
        return this;
    }

    public GuiBuilder addItem(Integer slot, ItemStack item) {
        slotsToFill.put(slot, item);
        return this;
    }

    public GuiBuilder setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public GuiBuilder setName(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public void fillSlotsInRange(ItemStack item, int from, int to) {
        for (int i = from; i <= to; i++) {
            slotsToFill.put(i, item);
        }
    }

    public void fillSlots(ItemStack item, int... slots) {
        for(int i : slots) {
            slotsToFill.put(i, item);
        }
    }




    public void open(Player... players) {
        Inventory inventory;
        if(name == null) {
            inventory = Bukkit.createInventory(null, rows * 9);
        }else {
            inventory = Bukkit.createInventory(null, rows * 9, name);
        }

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

        for(Map.Entry<Integer, ItemStack> entry : slotsToFill.entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();

            inventory.setItem(slot, item);
        }

        for(Player player : players) {
            player.openInventory(inventory);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<Player, InventoryManager> managers : whenClicked.entrySet()) {
                    WavesPluginLib.inventoryManager.put(managers.getKey(), new InventoryData(managers.getValue(), inventory));
                }
                for(Map.Entry<Player, CloseInventoryManager> managers : whenClosed.entrySet()) {
                    WavesPluginLib.closeInventoryManager.put(managers.getKey(), new CloseInventoryData(managers.getValue(), inventory));
                }
            }
        }.runTaskLater(WavesPluginLib.pluginInstance, 1);
    }

}
