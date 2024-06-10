package dk.wavebleak.wavespluginlib.labymodhelpers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class InputBoxResponseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String id;
    private final String value;

    public InputBoxResponseEvent(Player player, String id, String value) {
        this.player = player;
        this.id = id;
        this.value = value;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
