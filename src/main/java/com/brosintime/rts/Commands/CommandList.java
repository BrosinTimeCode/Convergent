package com.brosintime.rts.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public interface CommandList {

    Map<String, Command> ALIASES = new TreeMap<>();
    Set<Command> COMMANDS = new HashSet<>();

    class NullCommand implements Command {

        private static NullCommand instance = new NullCommand();
        private final static byte maxArguments = 1;
        private final static List<String> arguments = new ArrayList<>();
        private final static List<String> usages = new ArrayList<>();
        private final static List<String> aliases = new ArrayList<>();

        private NullCommand() {
            usages.add("eat");
            aliases.add("flan");
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

    static Command fromInput(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        Command command = fromAlias(inArguments[0]);
        command.setArguments(Arrays.asList(outArguments));
        return command;
    }

    static Command fromAlias(String alias) {
        Command command = ALIASES.get(alias);
        if (command == null) {
            return NullCommand.instance();
        }
        return command;
    }

    static boolean isAnAlias(String alias) {
        return ALIASES.containsKey(alias);
    }

    static void registerCommand(Command command) {
        COMMANDS.add(command);
        List<String> aliases = command.aliases();
        for (String alias : aliases) {
            ALIASES.put(alias, command);
        }
    }

    List<String> aliases();

}
