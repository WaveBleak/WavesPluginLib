package dk.wavebleak.wavespluginlib.anvilguihelper;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;

public class Wrapper implements VersionWrapper{
    @Override
    public int getNextContainerId(Player player, AnvilContainerWrapper container) {
        return toNMS(player).nextContainerCounter();
    }

    @Override
    public void handleInventoryCloseEvent(Player player) {
        CraftEventFactory.handleInventoryCloseEvent(toNMS(player));
        toNMS(player).p();
    }

    @Override
    public void sendPacketOpenWindow(Player player, int containerId, Object inventoryTitle) {
        toNMS(player)
                .playerConnection
                .sendPacket(new PacketPlayOutOpenWindow(
                        containerId, "minecraft:anvil", new ChatMessage(Blocks.ANVIL.a() + ".name")
                ));
    }

    @Override
    public void sendPacketCloseWindow(Player player, int containerId) {
        toNMS(player).playerConnection.sendPacket(new PacketPlayOutCloseWindow(containerId));
    }

    @Override
    public void setActiveContainerDefault(Player player) {
        toNMS(player).activeContainer = toNMS(player).defaultContainer;
    }

    @Override
    public void setActiveContainer(Player player, AnvilContainerWrapper container) {
        toNMS(player).activeContainer = (Container) container;
    }

    @Override
    public void setActiveContainerId(AnvilContainerWrapper container, int containerId) {
        ((Container)container).windowId = containerId;
    }

    @Override
    public void addActiveContainerSlotListener(AnvilContainerWrapper container, Player player) {
        ((Container)container).addSlotListener(toNMS(player));
    }

    @Override
    public AnvilContainerWrapper newContainerAnvil(Player player, Object title) {
        return new AnvilContainer(toNMS(player));
    }

    @Override
    public String literalChatComponent(String content) {
        return null;
    }

    @Override
    public String jsonChatComponent(String json) {
        return null;
    }

    private EntityPlayer toNMS(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
