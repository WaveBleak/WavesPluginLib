package dk.wavebleak.wavespluginlib.anvilguihelper;

import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public interface VersionWrapper {

    int getNextContainerId(Player player, AnvilContainerWrapper container);

    void handleInventoryCloseEvent(Player player);

    void sendPacketOpenWindow(Player player, int containerId, Object inventoryTitle);

    void sendPacketCloseWindow(Player player, int containerId);

    void setActiveContainerDefault(Player player);

    void setActiveContainer(Player player, AnvilContainerWrapper container);

    void setActiveContainerId(AnvilContainerWrapper container, int containerId);

    void addActiveContainerSlotListener(AnvilContainerWrapper container, Player player);

    AnvilContainerWrapper newContainerAnvil(Player player, Object title);

    default boolean isCustomTitleSupported() {
        return true;
    }

    String literalChatComponent(String content);

    String jsonChatComponent(String json);
}
