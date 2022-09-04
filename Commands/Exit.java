package Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exit extends Command {

    private final static byte maxArguments = 0;
    private final static List<String> arguments = new ArrayList<>();
    private final static Map<Integer, String> usages = new HashMap<>();

    public Exit() {
        usages.put(0, "");
        CommandList.registerAlias("exit", this);
        CommandList.registerAlias("quit", this);
        CommandList.registerAlias("stop", this);
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
}
