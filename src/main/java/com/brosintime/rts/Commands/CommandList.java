package com.brosintime.rts.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A command registrable and retrievable by aliases from a static list.
 * <p>
 * This interface stores commands and their aliases and provides methods to retrieve a command from
 * an alias or user input string. Before querying, commands need to be registered using the
 * {@code registerCommand} method.
 *
 * <p>Commands that implement this interface are required to keep a list of their aliases, which
 * are automatically registered when {@code registerCommand} is called.
 */
public interface CommandList {

    Map<String, Command> ALIASES = new TreeMap<>();
    Set<Command> COMMANDS = new HashSet<>();

    /**
     * A dummy command intended to take the place of null commands. All its fields and methods are
     * fail-safe, so in the event a null command is retrieved, an exception is avoided.
     */
    class NullCommand implements Command {

        private static NullCommand instance = new NullCommand();
        private final static byte maxArguments = 1;
        private final static List<String> arguments = new ArrayList<>();
        private final static List<String> usages = new ArrayList<>(List.of("eat"));
        private final static List<String> aliases = new ArrayList<>(List.of("flan"));

        private NullCommand() {
        }

        public static Command instance() {
            if (instance == null) {
                instance = new NullCommand();
            }
            return instance;
        }

        @Override
        public List<String> getArguments() {
            return arguments;
        }

        @Override
        public List<String> usages() {
            return usages;
        }

        @Override
        public String name() {
            return "Flan";
        }

        @Override
        public String defaultAlias() {
            return "flan";
        }

        @Override
        public String basicUsage() {
            return "flan eat";
        }

        @Override
        public String description() {
            return "Calorie-free flan.";
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
            return ArgStatus.GOOD;
        }

        @Override
        public String getArgument(int index) throws IndexOutOfBoundsException {
            return arguments.get(index);
        }

        @Override
        public List<String> aliases() {
            return aliases;
        }

        @Override
        public byte maxArguments() {
            return maxArguments;
        }

        @Override
        public void setArguments(List<String> args) {
            arguments.clear();
            arguments.addAll(args);
        }

    }

    /**
     * Retrieves a command instance from a user input string. The string is split by whitespace
     * characters, with the first substring acting as the command alias and any additional
     * substrings being assigned to the command as a list of arguments. If the string is null or a
     * command cannot be found, a NullCommand command is returned.
     *
     * @param input a string containing first a command alias and optionally followed by command
     *              arguments separated by spaces
     * @return the command associated with the given alias
     */
    static Command fromInput(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        Command command = fromAlias(inArguments[0]);
        command.setArguments(Arrays.asList(outArguments));
        return command;
    }

    /**
     * Retrieves a command instance from an alias registered in CommandList. If the alias does not
     * exist, a NullCommand is returned instead.
     *
     * @param alias a command alias
     * @return the command associated with the given alias, or NullCommand if not found
     */
    static Command fromAlias(String alias) {
        Command command = ALIASES.get(alias);
        if (command == null) {
            return NullCommand.instance();
        }
        return command;
    }

    /**
     * Determines whether the given alias is registered in ALIASES.
     *
     * @param alias a command alias
     * @return if the alias is registered
     */
    static boolean isAnAlias(String alias) {
        return ALIASES.containsKey(alias);
    }

    /**
     * Registers the command and its aliases in the global registry. If the alias already exists in
     * the registry, the aliases will be associated with the new command.
     *
     * @param command the command to register
     */
    static void registerCommand(Command command) {
        COMMANDS.add(command);
        List<String> aliases = command.aliases();
        for (String alias : aliases) {
            ALIASES.put(alias, command);
        }
    }

    /**
     * Retrieves a list of aliases from the command. This is called when registering the command
     * using the {@code registerCommand} method from {@code CommandList}.
     *
     * @return list of aliases
     */
    List<String> aliases();

}
