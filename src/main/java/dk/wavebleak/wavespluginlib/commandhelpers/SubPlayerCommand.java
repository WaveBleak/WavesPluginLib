package dk.wavebleak.wavespluginlib.commandhelpers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public abstract class SubPlayerCommand extends SubCommand{
    public SubPlayerCommand(Command command, String... names) {
        super(command, names);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        execute((Player) sender, args);
    }

    public abstract void execute(Player player, String[] args);

}
