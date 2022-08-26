package Commands;

public class Help extends BaseCommand {

    char identifier = 'h';
    String syntax = identifier + " [command]";
    String description = "Help: List of commands.";
    byte minArguments = 0;
    byte maxArguments = 1;

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
