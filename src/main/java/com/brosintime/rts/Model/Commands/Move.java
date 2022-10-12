package com.brosintime.rts.Model.Commands;

import java.util.ArrayList;
import java.util.List;

public class Move implements Command {

    private static Move instance = null;
    private final static byte maxArguments = 3;
    private final static List<String> arguments = new ArrayList<>();
    private final static List<String> usages = new ArrayList<>();
    private final static List<String> aliases = new ArrayList<>();

    private Move() {
        usages.add("");
        usages.add("(target ID)");
        usages.add("(x) (y)");
        usages.add("(unit ID) (x) (y)");
        aliases.add("move");
        aliases.add("m");
    }

    public static Move instance() {
        if (instance == null) {
            instance = new Move();
        }
        return instance;
    }

    @Override
    public void setArguments(List<String> args) {
        arguments.clear();
        arguments.addAll(args);
    }

    @Override
    public List<String> arguments() {
        return arguments;
    }

    @Override
    public List<String> usages() {
        return usages;
    }

    @Override
    public String name() {
        return "Move";
    }

    @Override
    public String defaultAlias() {
        return "move";
    }

    @Override
    public String basicUsage() {
        return "move (x) (y)";
    }

    @Override
    public String description() {
        return "Asks a (selected) unit to move to another or a square.";
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
    public String argument(int index) throws IndexOutOfBoundsException {
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
