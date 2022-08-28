package Commands;

public class Move extends BaseCommand {

    char identifier = 'm';
    String syntax = identifier + " ([unit]|[x]) ([x]|[y]) [y]";
    String description =
      "Move: Asks a (selected) unit to move to another unit's current square or a set of coordinates."
        + syntax;
    byte minArguments = 0;
    byte maxArguments = 3;
    String[] arguments;

    public Move(String[] args) {
        arguments = args;
    }

    @Override
    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String getSyntax() {
        return "Usage: " + syntax;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getError_tooManyArguments() {
        return tooManyArguments;
    }

    @Override
    public String getError_tooFewArguments() {
        return tooFewArguments;
    }

    @Override
    public boolean hasEnoughArguments(String[] arguments) {
        return arguments.length >= minArguments;
    }

    @Override
    public boolean hasTooManyArguments(String[] arguments) {
        return arguments.length > maxArguments;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }
}
