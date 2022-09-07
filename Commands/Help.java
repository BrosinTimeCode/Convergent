package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Help extends Command {

    private static Help instance = null;
    private final static byte maxArguments = 1;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();
    private final static List<String> aliases = new ArrayList<>();

    private Help() {
        usages.put(0, "");
        usages.put(1, "(command)");
        aliases.add("help");
        aliases.add("h");
        for (String alias : aliases) {
            CommandList.registerAlias(alias, this);
        }
    }

    public static Command getInstance() {
        if (instance == null) {
            instance = new Help();
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
    public ArgStatus validateArguments() {
        if (hasTooManyArguments()) {
            return ArgStatus.TOOMANY;
        }
        if (arguments.size() < 1) {
            return ArgStatus.NOARGS;
        }
        boolean isPositiveInt = false;
        try {
            int i = Integer.parseInt(arguments.get(0));
            if (i > 0) {
                isPositiveInt = true;
            }
        } catch (NumberFormatException nfe) {
            isPositiveInt = false;
        }
        if (CommandList.isAnAlias(arguments.get(0)) | isPositiveInt) {
            return ArgStatus.GOOD;
        }
        return ArgStatus.BAD;
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
