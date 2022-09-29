package com.brosintime.rts.Commands;

import java.util.ArrayList;
import java.util.List;

public class Attack implements Command {

    private static Attack instance = null;
    private final static byte maxArguments = 3;
    private final static List<String> arguments = new ArrayList<>();
    private final static List<String> usages = new ArrayList<>();
    private final static List<String> aliases = new ArrayList<>();

    private Attack() {
        usages.add("");
        usages.add("(target)");
        usages.add("(x) (y)");
        usages.add("(unit) (x) (y)");
        aliases.add("attack");
        aliases.add("atk");
        aliases.add("a");
    }

    public static Attack instance() {
        if (instance == null) {
            instance = new Attack();
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
        return "Attack";
    }

    @Override
    public String defaultAlias() {
        return "attack";
    }

    @Override
    public String basicUsage() {
        return "attack (target)";
    }

    @Override
    public String description() {
        return "Asks a (selected) unit to attack another.";
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
