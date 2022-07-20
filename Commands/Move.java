package Commands;

public class Move extends BaseCommand {
    char identifier = 'm';
    String syntax = identifier + " ([unit]|[x]) ([x]|[y]) [y]";
    String description = "Move: Asks a (selected) unit to move to another unit's current square or a set of coordinates." + syntax;
    byte minArguments = 0;
    byte maxArguments = 3;
}
