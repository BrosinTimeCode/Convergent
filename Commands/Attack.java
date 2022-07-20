package Commands;

public class Attack extends BaseCommand {
    char identifier = 'a';
    String syntax = identifier + " (unit) [[unit]|[x]] [y]";
    String description = "Attack: Asks a (selected) unit to attack another.";
    byte minArguments = 0;
    byte maxArguments = 3;
}
