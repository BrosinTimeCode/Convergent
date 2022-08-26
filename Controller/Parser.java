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

        try {
            char commandID = arguments[0].charAt(0);
            for (BaseCommand command : commands) {
                if (commandID == command.getIdentifier()) {
                    return command;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}
