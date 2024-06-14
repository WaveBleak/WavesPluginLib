package dk.wavebleak.wavespluginlib.anvilguihelper;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.Slot;
import org.bukkit.inventory.Inventory;

public class AnvilContainer extends ContainerAnvil implements AnvilContainerWrapper {

    public AnvilContainer(EntityHuman entityHuman) {
        super(entityHuman.inventory, entityHuman.world, new BlockPosition(0, 0, 0), entityHuman);
    }

    @Override
    public void e() {
        Slot output = this.getSlot(2);
        if(!output.hasItem()) {
            Slot input = this.getSlot(0);

            if(input.hasItem()) {
                output.set(input.getItem().cloneItemStack());
            }
        }

        this.a = 0;

        this.b();
    }

    @Override
    public boolean a(EntityHuman human) {
        return true;
    }

    @Override
    public void b(EntityHuman entityhuman) {}


    @Override
    public Inventory getBukkitInventory() {
        return getBukkitView().getTopInventory();
    }
}
