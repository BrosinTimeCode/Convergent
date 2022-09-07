package Commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface CommandList {

    Map<String, Command> ALIASES = new TreeMap<>();
    HashSet<Command> COMMANDS = new HashSet<>();

    enum ArgStatus {
        GOOD,
        TOOMANY,
        NOARGS,
        BAD
    }

    static void initializeCommands() {
        COMMANDS.add(Attack.getInstance());
        COMMANDS.add(Help.getInstance());
        COMMANDS.add(Move.getInstance());
        COMMANDS.add(Select.getInstance());
    }

    static Command getCommandFromInput(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        try {
            Command command = getCommandFromAlias(inArguments[0]);
            command.setArguments(Arrays.asList(outArguments));
            return command;
        } catch (Exception e) {
            return null;
        }
    }

    static Command getCommandFromAlias(String alias) {
        return ALIASES.get(alias);
    }

    static void registerAlias(String alias, Command command) {
        ALIASES.put(alias, command);
    }

    static boolean isAnAlias(String alias) {
        return ALIASES.containsKey(alias);
    }

    void setArguments(List<String> args);

    List<String> getArguments();

    Map<Integer, String> getUsages();

    String getName();

    String getDefaultAlias();

    String getBasicUsage();

    String getDescription();

    boolean hasTooManyArguments();

    ArgStatus validateArguments();

    String getArgument(int index) throws IndexOutOfBoundsException;

}
