package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exit extends Command {

    private static Exit instance = null;
    private final static byte maxArguments = 0;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();
    private final static List<String> aliases = new ArrayList<>();

    public Exit() {
        usages.put(0, "");
        aliases.add("exit");
        aliases.add("quit");
        aliases.add("stop");
        for (String alias : aliases) {
            CommandList.registerAlias(alias, this);
        }
    }

    public static Exit getInstance() {
        if (instance == null) {
            instance = new Exit();
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
        return "Exit";
    }

    @Override
    public String getDefaultAlias() {
        return "exit";
    }

    @Override
    public String getBasicUsage() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "Exits the game.";
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
        return ArgStatus.NOARGS;
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
