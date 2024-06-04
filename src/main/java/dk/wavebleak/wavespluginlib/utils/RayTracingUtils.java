package dk.wavebleak.wavespluginlib.utils;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RayTracingUtils {

    public static List<Player> rayTracePlayers(Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector()).normalize().multiply(0.1);
        double distance = start.distance(end);
        return rayTracePlayers(start, direction, distance);
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
        Entity entityPlayer = ((CraftPlayer) player).getHandle();

        return entityPlayer.getBoundingBox();
    }

}
