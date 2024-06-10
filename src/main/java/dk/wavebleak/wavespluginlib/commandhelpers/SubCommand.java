package dk.wavebleak.wavespluginlib.commandhelpers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public abstract class SubCommand {

    protected String[] names;
    protected List<SubCommand> subCommands = new ArrayList<>();
    protected Command command;

    public SubCommand(Command command, String... names) {
        this.command = command;
        this.names = names;
    }

    @SuppressWarnings("unused")
    protected SubCommand addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @SuppressWarnings("unused")
    public void callSubCommands(CommandSender sender, String[] args) {
        if(args.length == 0) return;
        for(SubCommand subCommand : subCommands) {
            for(String name : names) {
                if (name.equalsIgnoreCase(args[0])) {
                    subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                    return;
                }
            }
        }
    }

    public abstract void execute(CommandSender sender, String[] args);

}
