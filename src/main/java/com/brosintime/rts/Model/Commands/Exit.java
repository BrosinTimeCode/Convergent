package com.brosintime.rts.Model.Commands;

import java.util.ArrayList;
import java.util.List;

public class Exit implements Command {

    private static Exit instance = null;
    private final static byte maxArguments = 0;
    private final static List<String> arguments = new ArrayList<>();
    private final static List<String> usages = new ArrayList<>();
    private final static List<String> aliases = new ArrayList<>();

    private Exit() {
        usages.add("");
        aliases.add("exit");
        aliases.add("quit");
        aliases.add("stop");
    }

    public static Exit instance() {
        if (instance == null) {
            instance = new Exit();
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
        return "Exit";
    }

    @Override
    public String defaultAlias() {
        return "exit";
    }

    @Override
    public String basicUsage() {
        return "exit";
    }

    @Override
    public String description() {
        return "Exits the game.";
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
        return ArgStatus.NOARGS;
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
