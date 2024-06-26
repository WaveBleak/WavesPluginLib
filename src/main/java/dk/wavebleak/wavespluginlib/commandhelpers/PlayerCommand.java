package dk.wavebleak.wavespluginlib.commandhelpers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public abstract class PlayerCommand extends Command{
    @SuppressWarnings("unused")
    public PlayerCommand(JavaPlugin instance, String commandName) {
        super(instance, commandName);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        if(!callSubCommands(commandSender, strings)) execute((Player) commandSender, strings);
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {}

    public abstract void execute(Player player, String[] args);


}
