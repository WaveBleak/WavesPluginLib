package dk.wavebleak.wavespluginlib.commandhelpers;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@SuppressWarnings("unused")
public abstract class Command implements CommandExecutor {
    protected JavaPlugin instance;
    protected PluginCommand command;
    protected String commandName;
    protected List<SubCommand> subCommands = new ArrayList<>();
    public Command(JavaPlugin instance, String commandName) {
        this.instance = instance;
        this.commandName = commandName;
        if(instance.getCommand(commandName) != null) {
            command = instance.getCommand(commandName);
            command.setExecutor(this);
        }
    }

    @SuppressWarnings("unused")
    public Command addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        callSubCommands(commandSender, strings);
        execute(commandSender, strings);
        return true;
    }

    @SuppressWarnings("unused")
    public void callSubCommands(CommandSender sender, String[] args) {
        if(args.length == 0) return;
        for(SubCommand subCommand : subCommands) {
            for(String name : subCommand.names) {
                if (name.equalsIgnoreCase(args[0])) {
                    subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                    return;
                }
            }
        }
    }


    public abstract void execute(CommandSender sender, String[] args);
}
