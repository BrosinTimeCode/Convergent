package Commands;

public class Select extends BaseCommand {
    char identifier = 's';
    String syntax = identifier + " ([unit]|[x]) [y]";
    String description = "Select: Selects and highlights a unit based on ID or square coordinates.";
    byte minArguments = 0;
    byte maxArguments = 2;
}
