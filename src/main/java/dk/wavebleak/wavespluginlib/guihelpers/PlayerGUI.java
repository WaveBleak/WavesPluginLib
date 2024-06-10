package dk.wavebleak.wavespluginlib.guihelpers;


import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public abstract class PlayerGUI extends GUI{
    protected Player player;
    public PlayerGUI(Player player) {
        init(player);
    }

    @Override
    protected void init() {

    }
    protected abstract void init(Player player);
}
