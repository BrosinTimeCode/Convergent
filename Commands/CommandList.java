package Commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

public interface CommandList {

    TreeSet<Alias> COMMAND_ALIASES = new TreeSet<>();
    TreeSet<String> ALIASES = new TreeSet<>();
    HashSet<Command> COMMANDS = new HashSet<>();

    static void initializeCommands() {
        COMMANDS.add(new Attack());
        COMMANDS.add(new Help());
        COMMANDS.add(new Move());
        COMMANDS.add(new Select());
    }

    static Command getCommand(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        try {
            String commandAlias = inArguments[0];
            for (Alias alias : COMMAND_ALIASES) {
                if (commandAlias.equals(alias.toString())) {
                    Command command = alias.getCommand();
                    command.setArguments(Arrays.asList(outArguments));
                    return command;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    static void registerAlias(Alias alias) {
        COMMAND_ALIASES.add(alias);
        ALIASES.add(alias.toString());
    }

    static boolean isAnAlias(String alias) {
        return ALIASES.contains(alias);
    }
}
