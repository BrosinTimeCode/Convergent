package Commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

public class CommandList {

    private static final TreeSet<Alias> commandAliases = new TreeSet<>();
    private static final TreeSet<String> aliases = new TreeSet<>();
    private static final HashSet<Command> commands = new HashSet<>();

    public static void initializeCommands() {
        commands.add(new Attack());
        commands.add(new Help());
        commands.add(new Move());
        commands.add(new Select());
    }

    public static Command getCommand(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        try {
            String commandAlias = inArguments[0];
            for (Alias alias : commandAliases) {
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

    public static void registerAlias(Alias alias) {
        commandAliases.add(alias);
        aliases.add(alias.toString());
    }

    public static boolean isAnAlias(String alias) {
        return aliases.contains(alias);
    }
}
