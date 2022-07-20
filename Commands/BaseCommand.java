package Commands;

public abstract class BaseCommand {
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_BLUE = "\u001B[34m";
    final String ANSI_RED = "\u001B[31m";
    char identifier;
    String syntax = identifier + " (unit)";
    String description = "A command.";
    String tooManyArguments = ANSI_RED + "Too many arguments! Usage: " + syntax + ANSI_RESET;
    String tooFewArguments = ANSI_RED + "Too few arguments! Usage: " + syntax + ANSI_RESET;
    byte minArguments = 0;
    byte maxArguments = 1;

    public char getIdentifier() {
        return identifier;
    }

    public String getSyntax() {
        return "Usage: " + syntax;
    }

    public String getDescription() {
        return description;
    }

    public String getError_tooManyArguments() {
        return tooManyArguments;
    }

    public String getError_tooFewArguments() {
        return tooFewArguments;
    }

    public boolean hasEnoughArguments(String[] arguments) {
        if (arguments.length >= minArguments) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean hasTooManyArguments(String[] arguments) {
        if (arguments.length > maxArguments) {
            return true;
        }
        else {
            return false;
        }
    }
}
