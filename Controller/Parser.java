package Controller;

import Commands.*;

import java.util.ArrayList;

public class Parser {
    public static BaseCommand getCommand(String input) {
        String[] arguments = input.split("\\s+");

        ArrayList<BaseCommand> commands = new ArrayList<>();
        commands.add(new Attack());
        commands.add(new Help());
        commands.add(new Move());
        commands.add(new Select());

        char commandID = arguments[0].charAt(0);
        for (int i = 0; i < commands.size(); i++) {
            if (commandID == commands.get(i).getIdentifier()) {
                return commands.get(i);
            }
        }

        return null;
    }
}
