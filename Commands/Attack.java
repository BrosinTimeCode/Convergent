package Commands;

public class Attack extends BaseCommand {

    char identifier = 'a';
    String syntax = identifier + " (unit) [[unit]|[x]] [y]";
    String description = "Attack: Asks a (selected) unit to attack another.";
    byte minArguments = 0;
    byte maxArguments = 3;
    String[] arguments;

    public Attack(String[] args) {
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
