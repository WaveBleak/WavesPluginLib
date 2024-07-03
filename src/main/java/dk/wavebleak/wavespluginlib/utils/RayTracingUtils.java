package dk.wavebleak.wavespluginlib.utils;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class RayTracingUtils {

    public static Vector calculatePlayerHitVector(Location start, int maxLength, Entity... filter) {
        Vector dir = start.getDirection().normalize().multiply(0.01);

        Location current = start.clone();
        float currentLength = 0f;
        while(start.distance(current) < maxLength) {
            current.add(dir);
            currentLength += 0.01f;

            World nmsWorld = ((CraftWorld)current.getWorld()).getHandle();

            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                    current.getX() - 0.01,
                    current.getY() - 0.01,
                    current.getZ() - 0.01,
                    current.getX() + 0.01,
                    current.getY() + 0.01,
                    current.getZ() + 0.01
            );

            List<net.minecraft.server.v1_8_R3.Entity> nmsEntityList = nmsWorld.getEntities(null, axisAlignedBB);

            List<Entity> collidedEntities = nmsEntityList.stream()
                    .map(net.minecraft.server.v1_8_R3.Entity::getBukkitEntity)
                    .collect(Collectors.toList());

            for(Entity entity : collidedEntities) {
                if(Arrays.asList(filter).contains(entity)) continue;
                return dir.normalize().multiply(currentLength);
            }

        }
        return null;
    }
    public static List<Player> rayTracePlayers(Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = start.distance(end);
        return rayTracePlayers(start, direction, distance);
    }

    public static Player rayTracePlayer(Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = start.distance(end);
        return rayTracePlayer(start, direction, distance);
    }


    public static List<Player> rayTracePlayers(Location start, Vector direction, double distance) {
        List<Player> hitPlayers = new ArrayList<>();

        direction = direction.normalize().multiply(0.1);

        Location current = start.clone();
        for(float i = 0.1f; i < distance ; i += 0.1f) {
            current.add(direction);

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(isPlayerInLocation(player, current) && !hitPlayers.contains(player)) {
                    hitPlayers.add(player);
                }
            }
        }

        return hitPlayers;
    }

    public static List<Block> rayTraceBlocks(Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = start.distance(end);
        return rayTraceBlocks(start, direction, distance);
    }

    public static List<Block> rayTraceBlocks(Location start, Vector direction, double distance) {
        List<Block> hitBlocks = new ArrayList<>();

        direction = direction.normalize().multiply(0.1);

        Location current = start.clone();

        for(float i = 0.1f; i < distance; i += 0.1f) {
            current.add(direction);
            if(!current.getBlock().getType().isSolid()) continue;
            hitBlocks.add(current.getBlock());
        }

        return hitBlocks;
    }
    public static Block rayTraceBlock(Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = start.distance(end);
        return rayTraceBlock(start, direction, distance);
    }

    public static Block rayTraceBlock(Location start, Vector direction, double distance) {
        direction = direction.normalize().multiply(0.1);

        Location current = start.clone();

        for(float i = 0.1f; i < distance; i += 0.1f) {
            current.add(direction);
            if(!current.getBlock().getType().isSolid()) continue;
            return current.getBlock();
        }

        return null;
    }

    public static Player rayTracePlayer(Location start, Vector direction, double distance) {
        direction = direction.normalize().multiply(0.1);

        Location current = start.clone();
        for(float i = 0.1f; i < distance ; i += 0.1f) {
            current = current.add(direction);
            for(Player player : Bukkit.getOnlinePlayers()) {
                if (isPlayerInLocation(player, current)) {
                    return player;
                }
            }
        }
        return null;
    }

    public static boolean isPlayerInLocation(Player player, Location location) {
        AxisAlignedBB playerBoundingBox = getBoundingBox(player);

        return isLocationInBoundingBox(location, playerBoundingBox);
    }

    public static boolean isLocationInBoundingBox(Location loc, AxisAlignedBB boundingBox) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return x >= boundingBox.a && x <= boundingBox.d && y >= boundingBox.b && y <= boundingBox.e && z >= boundingBox.c && z <= boundingBox.f;
    }

    public static AxisAlignedBB getBoundingBox(Player player) {
        net.minecraft.server.v1_8_R3.Entity entityPlayer = ((CraftPlayer)player).getHandle();

        return entityPlayer.getBoundingBox();
    }

}
