package dk.wavebleak.wavespluginlib.utils;

import net.minecraft.server.v1_8_R3.Vec3D;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Raytrace {
    private final Location start;
    private final Vector angle;
    private final double length;
    private List<Object> whitelist;
    private boolean stopOnFirstHit;
    public TriConsumer<Block, BlockFace, Location> onHitBlock = (block, face, loc) -> {};
    public BiConsumer<Entity, Location>  onHitEntity = (entity, loc) -> {};
    public Consumer<Location> onHitEnd = loc -> {};
    public Raytrace(Location start, Vector angle, double length) {
        this.start = start;
        this.angle = angle.normalize();
        this.length = length;
        this.whitelist = new ArrayList<>();
        this.stopOnFirstHit = false;
    }

    public Raytrace setWhiteList(List<Object> whitelist) {
        this.whitelist = whitelist;
        return this;
    }

    public Raytrace addToWhiteList(Object... objects) {
        whitelist.addAll(Arrays.stream(objects).collect(Collectors.toList()));
        return this;
    }

    public Raytrace stopOnFirstHit() {
        this.stopOnFirstHit = true;
        return this;
    }

    public Raytrace onHitBlock(TriConsumer<Block, BlockFace, Location> onHitBlock) {
        this.onHitBlock = onHitBlock;
        return this;
    }

    public Raytrace onHitEntity(BiConsumer<Entity, Location> runnable) {
        this.onHitEntity = runnable;
        return this;
    }

    public Raytrace onHitEnd(Consumer<Location> runnable) {
        this.onHitEnd = runnable;
        return this;
    }

    public void raytrace() {
        Location previous = this.start.clone();
        Vector angle = this.angle.clone().multiply(0.01);
        Location current = previous.clone().add(angle);

        for(double i = 0; i < length; i += 0.01) {
            previous = current;
            current.add(angle);

            Block block = current.getBlock();
            Block previousBlock = previous.getBlock();
            Material type = block.getType();
            if(!whitelist.contains(block) && !whitelist.contains(type)) {
                BlockFace blockFace = previousBlock.getFace(block);
                onHitBlock.accept(block, blockFace, current); //add block face
                if(stopOnFirstHit) {
                    onHitEnd.accept(current);
                    return;
                }
            }

            Vec3D currentNMSLoc = new Vec3D(current.getX(), current.getY(), current.getZ());


            for(Entity entity : current.getWorld().getEntities()) {
                if(whitelist.contains(entity)) continue;
                net.minecraft.server.v1_8_R3.Entity nmsEntity = (net.minecraft.server.v1_8_R3.Entity) entity;

                if(whitelist.contains(nmsEntity)) continue;

                if(nmsEntity.getBoundingBox().a(currentNMSLoc)) {
                    onHitEntity.accept(entity, current);
                    if(stopOnFirstHit) {
                        onHitEnd.accept(current);
                        return;
                    }
                }
            }
        }

        onHitEnd.accept(current);
    }
}
