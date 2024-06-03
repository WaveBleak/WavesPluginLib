package dk.wavebleak.wavespluginlib.commandhelpers;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class SubCommand {

    protected String name;
    protected List<SubCommand> subCommands = new ArrayList<>();

    public SubCommand(String name) {
        this.name = name;
    }

    protected SubCommand addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    public void callSubCommands(String[] args) {
        if(args.length == 0) return;
        for(SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                subCommand.execute(Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }
    }

    public abstract void execute(String[] args);

}
