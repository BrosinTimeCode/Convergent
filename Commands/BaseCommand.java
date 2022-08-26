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

    public abstract char getIdentifier();

    public abstract String getSyntax();

    public abstract String getDescription();

    public abstract String getError_tooManyArguments();

    public abstract String getError_tooFewArguments();

    public abstract boolean hasEnoughArguments(String[] arguments);

    public abstract boolean hasTooManyArguments(String[] arguments);
}
