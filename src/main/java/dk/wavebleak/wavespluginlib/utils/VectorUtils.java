package dk.wavebleak.wavespluginlib.utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class VectorUtils {

    public static Vector reflect(Vector vec, Vector angle) {
        double dot = vec.dot(angle);
        Vector scaledAngle = angle.multiply(2 * dot);
        return vec.subtract(scaledAngle);
    }

    public static double angleBetweenVectors(Vector v1, Vector v2) {
        return Math.acos(v1.dot(v2) / (v1.length() * v2.length()));
    }

    public static Vector between(Location from, Location to) {
        return to.toVector().subtract(from.toVector());
    }





}
