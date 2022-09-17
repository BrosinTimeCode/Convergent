package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select extends Command {

    private static Select instance = null;
    private final static byte maxArguments = 2;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();
    private final static List<String> aliases = new ArrayList<>();

    private Select() {
        usages.put(0, "");
        usages.put(1, "(unit)");
        usages.put(2, "(x) (y)");
        aliases.add("select");
        aliases.add("sel");
        aliases.add("s");
        for (String alias : aliases) {
            CommandList.registerAlias(alias, this);
        }
    }

    public static Select getInstance() {
        if (instance == null) {
            instance = new Select();
        }
        return instance;
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
        return "select (unit)";
    }

    @Override
    public String getDescription() {
        return "Selects or deselects a unit.";
    }

    @Override
    public boolean hasTooManyArguments() {
        return arguments.size() > maxArguments;
    }

    @Override
    public ArgStatus validateArguments() {
        if (hasTooManyArguments()) {
            return ArgStatus.TOOMANY;
        }
        if (arguments.size() < 1) {
            return ArgStatus.NOARGS;
        }
        try {
            for (String arg : arguments) {
                int i = Integer.parseInt(arg);
                if (i < 0) {
                    return ArgStatus.BAD;
                }
            }
            return ArgStatus.GOOD;
        } catch (NumberFormatException nfe) {
            return ArgStatus.BAD;
        }
    }

    @Override
    public String getArgument(int index) throws IndexOutOfBoundsException {
        return arguments.get(index);
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public byte getMaxArguments() {
        return maxArguments;
    }

}
