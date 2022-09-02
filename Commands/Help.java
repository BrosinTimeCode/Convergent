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
        CommandList.registerAlias("help", this);
        CommandList.registerAlias("h", this);
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
    public String getName() {
        return "Help";
    }

    @Override
    public String getDefaultAlias() {
        return "help";
    }

    @Override
    public String getBasicUsage() {
        return "help [command]";
    }

    @Override
    public String getDescription() {
        return "Get a list of commands or help on a specific one.";
    }

    @Override
    public boolean hasTooManyArguments() {
        return arguments.size() > maxArguments;
    }

    @Override
    public byte validateArguments() {
        if (hasTooManyArguments()) {
            return 99;
        } else if (arguments.size() < 1) {
            return 0;
        } else if (CommandList.isAnAlias(arguments.get(0))) {
            return 1;
        }
        return 98;
    }

    @Override
    public String getArgument(int index) throws IndexOutOfBoundsException {
        return arguments.get(index);
    }

}
