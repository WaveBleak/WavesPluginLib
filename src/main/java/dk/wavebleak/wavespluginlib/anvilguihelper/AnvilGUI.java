package dk.wavebleak.wavespluginlib.anvilguihelper;

import dk.wavebleak.wavespluginlib.WavesPluginLib;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class AnvilGUI {
    private static final VersionWrapper WRAPPER = new Wrapper();

    private static final ItemStack AIR = new ItemStack(Material.AIR);

    private static ItemStack itemNotNull(ItemStack stack) {
        return stack == null ? AIR : stack;
    }

    private final JavaPlugin plugin = WavesPluginLib.pluginInstance;

    private final Player player;

    private final ItemStack[] initialContents;

    private final boolean preventClose;

    private final Set<Integer> interactableSlots;


    private final Consumer<StateSnapshot> closeListener;

    private final boolean concurrentClickHandlerExecution;

    private final ClickHandler clickHandler;

    private int containerId;

    @Getter
    private Inventory inventory;

    private final ListenUp listener = new ListenUp();

    private boolean open;

    private AnvilContainerWrapper container;



    public AnvilGUI(Player player, ItemStack[] initialContents, boolean preventClose, Set<Integer> interactableSlots, Consumer<StateSnapshot> closeListener, boolean concurrentClickHandlerExecution, ClickHandler clickHandler) {
        this.player = player;
        this.initialContents = initialContents;
        this.preventClose = preventClose;
        this.interactableSlots = Collections.unmodifiableSet(interactableSlots);
        this.closeListener = closeListener;
        this.concurrentClickHandlerExecution = concurrentClickHandlerExecution;
        this.clickHandler = clickHandler;
    }

    public void openInventory() {
        Bukkit.getPluginManager().registerEvents(listener, plugin);

        container = WRAPPER.newContainerAnvil(player, null);

        inventory = container.getBukkitInventory();

        for(int i = 0; i < initialContents.length; i++) {
            inventory.setItem(i, initialContents[i]);
        }

        containerId = WRAPPER.getNextContainerId(player, container);
        WRAPPER.handleInventoryCloseEvent(player);
        WRAPPER.sendPacketOpenWindow(player, containerId, null);
        WRAPPER.setActiveContainer(player, container);
        WRAPPER.setActiveContainerId(container, containerId);
        WRAPPER.addActiveContainerSlotListener(container, player);

        open = true;
    }

    public void closeInventory() {
        closeInventory(true);
    }

    private void closeInventory(boolean sendClosePacket) {
        if (!open) {
            return;
        }

        open = false;

        HandlerList.unregisterAll(listener);

        if (sendClosePacket) {
            WRAPPER.handleInventoryCloseEvent(player);
            WRAPPER.setActiveContainerDefault(player);
            WRAPPER.sendPacketCloseWindow(player, containerId);
        }

        if (closeListener != null) {
            closeListener.accept(StateSnapshot.fromAnvilGUI(this));
        }
    }



    private class ListenUp implements Listener {

        /**
         * Boolean storing the running status of the latest click handler to prevent double execution.
         * All accesses to this boolean will be from the main server thread, except for the rare event
         * that the plugin is disabled and the mainThreadExecutor throws an exception
         */
        private boolean clickHandlerRunning = false;

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!event.getInventory().equals(inventory)) {
                return;
            }

            final int rawSlot = event.getRawSlot();
            // ignore items dropped outside the window
            if (rawSlot == -999) return;

            final Player clicker = (Player) event.getWhoClicked();
            final Inventory clickedInventory = event.getClickedInventory();

            if (clickedInventory != null) {
                if (clickedInventory.equals(clicker.getInventory())) {
                    // prevent players from merging items from the anvil inventory
                    if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
                        event.setCancelled(true);
                        return;
                    }
                    // prevent shift moving items from players inv to the anvil inventory
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                        return;
                    }
                }
                // prevent players from swapping items in the anvil gui
                if ((event.getCursor() != null && event.getCursor().getType() != Material.AIR)
                        && !interactableSlots.contains(rawSlot)
                        && event.getClickedInventory().equals(inventory)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (rawSlot < 3 && rawSlot >= 0 || event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                event.setCancelled(!interactableSlots.contains(rawSlot));
                if (clickHandlerRunning && !concurrentClickHandlerExecution) {
                    // A click handler is running, don't launch another one
                    return;
                }
                final CompletableFuture<List<ResponseAction>> actionsFuture =
                        clickHandler.apply(rawSlot, StateSnapshot.fromAnvilGUI(AnvilGUI.this));

                final Consumer<List<ResponseAction>> actionsConsumer = actions -> {
                    for (final ResponseAction action : actions) {
                        action.accept(AnvilGUI.this, clicker);
                    }
                };

                if(actionsFuture == null) {
                    return;
                }

                if (actionsFuture.isDone()) {
                    // Fast-path without scheduling if clickHandler is performed in sync
                    // Because the future is already completed, .join() will not block the server thread
                    actionsFuture.thenAccept(actionsConsumer).join();
                } else {
                    clickHandlerRunning = true;
                    // If the plugin is disabled and the Executor throws an exception, the exception will be passed to
                    // the .handle method
                    actionsFuture
                            .thenAcceptAsync(actionsConsumer, WavesPluginLib.mainThreadExecutor)
                            .handle((results, exception) -> {
                                // Whether an exception occurred or not, set running to false
                                clickHandlerRunning = false;
                                return null;
                            });
                }
            }
        }

        @EventHandler
        public void onInventoryDrag(InventoryDragEvent event) {
            if (event.getInventory().equals(inventory)) {
                for (int slot : Slot.values()) {
                    if (event.getRawSlots().contains(slot)) {
                        event.setCancelled(!interactableSlots.contains(slot));
                        break;
                    }
                }
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (open && event.getInventory().equals(inventory)) {
                closeInventory(false);
                if (preventClose) {
                    WavesPluginLib.mainThreadExecutor.execute(AnvilGUI.this::openInventory);
                }
            }
        }
    }

    @FunctionalInterface
    public interface ClickHandler extends BiFunction<Integer, StateSnapshot, CompletableFuture<List<ResponseAction>>> {}

    public static final class StateSnapshot {

        /**
         * Create an {@link StateSnapshot} from the current state of an {@link AnvilGUI}
         * @param anvilGUI The instance to take the snapshot of
         * @return The snapshot
         */
        private static StateSnapshot fromAnvilGUI(AnvilGUI anvilGUI) {
            final Inventory inventory = anvilGUI.getInventory();
            return new StateSnapshot(
                    itemNotNull(inventory.getItem(Slot.INPUT_LEFT)).clone(),
                    itemNotNull(inventory.getItem(Slot.INPUT_RIGHT)).clone(),
                    itemNotNull(inventory.getItem(Slot.OUTPUT)).clone(),
                    anvilGUI.player);
        }

        /**
         * The {@link ItemStack} in the anvilGui slots
         */
        private final ItemStack leftItem, rightItem, outputItem;

        /**
         * The {@link Player} that clicked the output slot
         */
        private final Player player;

        /**
         * The event parameter constructor
         * @param leftItem The left item in the combine slot of the anvilGUI
         * @param rightItem The right item in the combine slot of the anvilGUI
         * @param outputItem The item that would have been outputted, when the items would have been combined
         * @param player The player that clicked the output slot
         */
        public StateSnapshot(ItemStack leftItem, ItemStack rightItem, ItemStack outputItem, Player player) {
            this.leftItem = leftItem;
            this.rightItem = rightItem;
            this.outputItem = outputItem;
            this.player = player;
        }

        /**
         * It returns the item in the left combine slot of the gui
         *
         * @return The leftItem
         */
        public ItemStack getLeftItem() {
            return leftItem;
        }

        /**
         * It returns the item in the right combine slot of the gui
         *
         * @return The rightItem
         */
        public ItemStack getRightItem() {
            return rightItem;
        }

        /**
         * It returns the output item that would have been the result
         * by combining the left and right one
         *
         * @return The outputItem
         */
        public ItemStack getOutputItem() {
            return outputItem;
        }

        /**
         * It returns the player that clicked onto the output slot
         *
         * @return The player
         */
        public Player getPlayer() {
            return player;
        }

        /**
         * It returns the text the player typed into the rename field
         *
         * @return The text of the rename field
         */
        public String getText() {
            return outputItem.hasItemMeta() ? outputItem.getItemMeta().getDisplayName() : "";
        }
    }
    public static class Slot {

        private static final int[] values = new int[] {Slot.INPUT_LEFT, Slot.INPUT_RIGHT, Slot.OUTPUT};

        /**
         * The slot on the far left, where the first input is inserted. An {@link ItemStack} is always inserted
         * here to be renamed
         */
        public static final int INPUT_LEFT = 0;
        /**
         * Not used, but in a real anvil you are able to put the second item you want to combine here
         */
        public static final int INPUT_RIGHT = 1;
        /**
         * The output slot, where an item is put when two items are combined from {@link #INPUT_LEFT} and
         * {@link #INPUT_RIGHT} or {@link #INPUT_LEFT} is renamed
         */
        public static final int OUTPUT = 2;

        /**
         * Get all anvil slot values
         *
         * @return The array containing all possible anvil slots
         */
        public static int[] values() {
            return values;
        }
    }
    @FunctionalInterface
    public interface ResponseAction extends BiConsumer<AnvilGUI, Player> {


        static ResponseAction replaceInputText(String text) {
            Validate.notNull(text, "text cannot be null");
            return (anvilgui, player) -> {
                ItemStack item = anvilgui.getInventory().getItem(Slot.OUTPUT);
                if (item == null) {
                    // Fallback on left input slot if player hasn't typed anything yet
                    item = anvilgui.getInventory().getItem(Slot.INPUT_LEFT);
                }
                if (item == null) {
                    throw new IllegalStateException(
                            "replaceInputText can only be used if slots OUTPUT or INPUT_LEFT are not empty");
                }

                final ItemStack cloned = item.clone();
                final ItemMeta meta = cloned.getItemMeta();
                meta.setDisplayName(text);
                cloned.setItemMeta(meta);
                anvilgui.getInventory().setItem(Slot.INPUT_LEFT, cloned);
            };
        }




        static ResponseAction openInventory(Inventory otherInventory) {
            Validate.notNull(otherInventory, "otherInventory cannot be null");
            return (anvilgui, player) -> player.openInventory(otherInventory);
        }


        static ResponseAction close() {
            return (anvilgui, player) -> anvilgui.closeInventory();
        }


        static ResponseAction run(Runnable runnable) {
            Validate.notNull(runnable, "runnable cannot be null");
            return (anvilgui, player) -> runnable.run();
        }
    }
}
