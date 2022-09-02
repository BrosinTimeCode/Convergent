package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select extends Command {

    private final static byte minArguments = 0;
    private final static byte maxArguments = 2;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();

    public Select() {
        usages.put(0, "");
        usages.put(1, "(unit)");
        usages.put(2, "(x) (y)");
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
