package com.brosintime.rts.Commands;

import java.util.ArrayList;
import java.util.List;

public class Help implements Command {

    private static Help instance = null;
    private final static byte maxArguments = 1;
    private final static List<String> arguments = new ArrayList<>();
    private final static List<String> usages = new ArrayList<>();
    private final static List<String> aliases = new ArrayList<>();

    private Help() {
        usages.add("");
        usages.add("(command)");
        aliases.add("help");
        aliases.add("h");
    }

    public static Help instance() {
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
    public List<String> usages() {
        return usages;
    }

    @Override
    public String name() {
        return "Help";
    }

    @Override
    public String defaultAlias() {
        return "help";
    }

    @Override
    public String basicUsage() {
        return "help [command]";
    }

    @Override
    public String description() {
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
        if (index < 0 || index > arguments.size()) {
            throw new IndexOutOfBoundsException();
        }
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

}
