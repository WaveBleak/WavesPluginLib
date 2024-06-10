package dk.wavebleak.wavespluginlib.utils;

import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class InventoryUtils {

    public static List<Integer> getSlotsInBox(int startCoord, int numberOfSlots) {
        List<Integer> ints = new ArrayList<>();
        int sqrt = (int)Math.round(Math.sqrt(numberOfSlots));

        for (int j = startCoord; j < startCoord + (9 * sqrt); j += 9) {
            for (int i = 0; i < sqrt; i++) {
                ints.add(j + i);
            }
        }

        return ints;
    }
    public static List<Integer> getBorderSlots(Inventory inv) {
        List<Integer> toReturn = new ArrayList<>();
        int inventorySize = 9;
        int rows = inv.getSize() / 9;

        IntStream.range(0, inventorySize).forEach(i -> {
            toReturn.add(i);
            toReturn.add(inventorySize * (rows - 1) + i);
        });


        IntStream.range(1, rows).forEach(i -> {
            int base = i * inventorySize;
            toReturn.add(base);
            toReturn.add(base + inventorySize - 1);
        });
        return toReturn;
    }

}
