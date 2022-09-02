package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Help extends Command {

    private final static byte maxArguments = 1;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();

    public Help() {
        usages.put(0, "");
        usages.put(1, "(command)");
        CommandList.registerAlias(new Alias("help", this));
        CommandList.registerAlias(new Alias("h", this));
    }

    @Override
    public void setArguments(List<String> args) {
        arguments.clear();
        arguments.addAll(args);
    }

    @Override
    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public Map<Integer, String> getUsages() {
        return usages;
    }

    @Override
    public String getDescription() {
        return "Help - Get a list of commands or help on a specific one";
    }

    @Override
    public boolean hasTooManyArguments() {
        return arguments.size() > maxArguments;
    }

    @Override
    public byte validateArguments() {
        if (hasTooManyArguments()) {
            return 99;
        }
        if (CommandList.isAnAlias(arguments.get(0))) {
            return 0;
        }
        return 98;
    }

}
