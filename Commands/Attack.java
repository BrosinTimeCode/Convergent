package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attack extends Command {

    private static Attack instance = null;
    private final static byte maxArguments = 3;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();
    private final static List<String> aliases = new ArrayList<>();

    private Attack() {
        usages.put(0, "");
        usages.put(1, "(target)");
        usages.put(2, "(x) (y)");
        usages.put(3, "(unit) (x) (y)");
        aliases.add("attack");
        aliases.add("atk");
        aliases.add("a");
        for (String alias : aliases) {
            CommandList.registerAlias(alias, this);
        }
    }

    public static Attack getInstance() {
        if (instance == null) {
            instance = new Attack();
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
        return "Attack";
    }

    @Override
    public String getDefaultAlias() {
        return "attack";
    }

    @Override
    public String getBasicUsage() {
        return "attack (target)";
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

}
