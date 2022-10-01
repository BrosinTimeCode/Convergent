package com.brosintime.rts.Commands;

import java.util.ArrayList;
import java.util.List;

public class Select implements Command {

    private static Select instance = null;
    private final static byte maxArguments = 2;
    private final static List<String> arguments = new ArrayList<>();
    private final static List<String> usages = new ArrayList<>();
    private final static List<String> aliases = new ArrayList<>();

    private Select() {
        usages.add("");
        usages.add("(unit ID)");
        usages.add("(x) (y)");
        aliases.add("select");
        aliases.add("sel");
        aliases.add("s");
    }

    public static Select instance() {
        if (instance == null) {
            instance = new Select();
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
        return "Select";
    }

    @Override
    public String defaultAlias() {
        return "select";
    }

    @Override
    public String basicUsage() {
        return "select (unit)";
    }

    @Override
    public String description() {
        return "Selects or deselects a unit.";
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
        try {
            for (String arg : arguments) {
                int i = Integer.parseInt(arg);
                if (i < 0) {
                    return ArgStatus.BAD;
                }
            }
            return ArgStatus.GOOD;
        } catch (NumberFormatException nfe) {
            return ArgStatus.BAD;
        }
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
