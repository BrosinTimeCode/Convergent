package Commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public interface CommandList {

    Map<String, Command> ALIASES = new TreeMap<>();
    HashSet<Command> COMMANDS = new HashSet<>();

    static void initializeCommands() {
        COMMANDS.add(new Attack());
        COMMANDS.add(new Help());
        COMMANDS.add(new Move());
        COMMANDS.add(new Select());
    }

    static Command getCommandFromAlias(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        try {
            Command command = getCommand(inArguments[0]);
            command.setArguments(Arrays.asList(outArguments));
            return command;
        } catch (Exception e) {
            return null;
        }
    }

    static Command getCommand(String alias) {
        return ALIASES.get(alias);
    }

    static void registerAlias(String alias, Command command) {
        ALIASES.put(alias, command);
    }

    static boolean isAnAlias(String alias) {
        return ALIASES.containsKey(alias);
    }
}
