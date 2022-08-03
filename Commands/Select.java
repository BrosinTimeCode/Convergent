package Commands;

public class Select extends BaseCommand {
    char identifier = 's';
    String syntax = identifier + " ([unit]|[x]) [y]";
    String description = "Select: Selects and highlights a unit based on ID or square coordinates.";
    byte minArguments = 0;
    byte maxArguments = 2;

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
}
