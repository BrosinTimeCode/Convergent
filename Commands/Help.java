package Commands;

public class Help extends BaseCommand {
    char identifier = 'h';
    String syntax = identifier + " [command]";
    String description = "Help: List of commands.";
    byte minArguments = 0;
    byte maxArguments = 1;
}
