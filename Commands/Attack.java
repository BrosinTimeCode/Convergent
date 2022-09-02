package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attack extends Command {

    private final static byte minArguments = 0;
    private final static byte maxArguments = 3;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();

    public Attack() {
        usages.put(0, "");
        usages.put(1, "(target)");
        usages.put(2, "(unit) (target)");
        usages.put(3, "(unit) (x) (y)");
        CommandList.registerAlias(new Alias("attack", this));
        CommandList.registerAlias(new Alias("atk", this));
        CommandList.registerAlias(new Alias("a", this));
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
        return "Attack - Asks a (selected) unit to attack another";
    }

    @Override
    public boolean hasEnoughArguments() {
        return arguments.size() >= minArguments;
    }

    @Override
    public boolean hasTooManyArguments() {
        return arguments.size() > maxArguments;
    }

}
