package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select extends Command {

    private final static byte maxArguments = 2;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();

    public Select() {
        usages.put(0, "");
        usages.put(1, "(unit)");
        usages.put(2, "(x) (y)");
        CommandList.registerAlias("select", this);
        CommandList.registerAlias("sel", this);
        CommandList.registerAlias("s", this);
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
        return "Select";
    }

    @Override
    public String getDefaultAlias() {
        return "select";
    }

    @Override
    public String getBasicUsage() {
        return "select [unit]";
    }

    @Override
    public String getDescription() {
        return "Asks a (selected) unit to attack another.";
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
        byte argIndex = 0;
        try {
            for (String arg : arguments) {
                int i = Integer.parseInt(arg);
                argIndex++;
            }
            return 0;
        } catch (NumberFormatException nfe) {
            return argIndex;
        }
    }

    @Override
    public String getArgument(int index) throws IndexOutOfBoundsException {
        return arguments.get(index);
    }

}
