package dk.wavebleak.wavespluginlib.anvilguihelper;

import dk.wavebleak.wavespluginlib.WavesPluginLib;

import dk.wavebleak.wavespluginlib.itemhelpers.ItemBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@SuppressWarnings("all")
public class AnvilGUIBuilder {

    private Consumer<AnvilGUI.StateSnapshot> closeListener;

    private AnvilGUI.ClickHandler clickHandler;

    private boolean preventClose = false;

    private Set<Integer> interactableSlots = Collections.emptySet();

    private final JavaPlugin plugin = WavesPluginLib.pluginInstance;

    private String itemText;

    private ItemStack itemLeft;

    private ItemStack itemRight;

    private ItemStack itemOutput;


    public AnvilGUIBuilder preventClose() {
        preventClose = true;
        return this;
    }

    public AnvilGUIBuilder interactableSlots(int... slots) {
        interactableSlots = Arrays.stream(slots).boxed().collect(Collectors.toSet());
        return this;
    }

    public AnvilGUIBuilder onClose(Consumer<AnvilGUI.StateSnapshot> closeListener) {
        Validate.notNull(closeListener, "closeListener cannot be null");
        this.closeListener = closeListener;
        return this;
    }

    public AnvilGUIBuilder onClickAsync(AnvilGUI.ClickHandler clickHandler) {
        Validate.notNull(clickHandler, "click function cannot be null");
        this.clickHandler = clickHandler;
        return this;
    }

    public AnvilGUIBuilder onClick(BiFunction<Integer, AnvilGUI.StateSnapshot, List<AnvilGUI.ResponseAction>> clickHandler) {
        Validate.notNull(clickHandler, "click function cannot be null");
        this.clickHandler =
                (slot, stateSnapshot) -> CompletableFuture.completedFuture(clickHandler.apply(slot, stateSnapshot));
        return this;
    }

    public AnvilGUIBuilder initialItemText(String text) {
        this.itemText = text;
        return this;
    }

    public AnvilGUIBuilder itemLeft(ItemStack item) {
        Validate.notNull(item, "item cannot be null");
        this.itemLeft = item;
        return this;
    }

    public AnvilGUIBuilder itemRight(ItemStack item) {
        Validate.notNull(item, "item cannot be null");
        this.itemRight = item;
        return this;
    }

    public AnvilGUIBuilder output(ItemStack item) {
        this.itemOutput = item;
        return this;
    }

    public AnvilGUI open(Player player) {
        Validate.notNull(plugin, "Du har ikke registreret pluginnet!");
        Validate.notNull(player, "player er null!");

        if(itemText != null) {
            if (itemLeft == null) {
                itemLeft = new ItemStack(Material.EMPTY_MAP);
            }

            itemLeft = new ItemBuilder()
                    .fromExistingItem(itemLeft)
                    .setName(itemText)
                    .build();
        }

        final AnvilGUI anvilGUI = new AnvilGUI(
                player,
                new ItemStack[] {itemLeft, itemRight, itemOutput},
                preventClose,
                interactableSlots,
                closeListener,
                false,
                clickHandler
        );
        anvilGUI.openInventory();
        return anvilGUI;
    }











}
