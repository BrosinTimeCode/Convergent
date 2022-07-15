package Commands;

public abstract class BaseCommand {
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_BLUE = "\u001B[34m";
    final String ANSI_RED = "\u001B[31m";
    char identifier;
    String description = "A command.";
    String example = identifier + " 5";
    String syntax = identifier + " (unit)";
    String tooManyArguments = ANSI_RED + "Too many arguments! Usage: " + syntax + ANSI_RESET;
    byte minArguments = 0;
    byte maxArguments = 1;
}
