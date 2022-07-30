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

//        System.out.println("Number of arguments: " + arguments.length);
//        System.out.println("Argument 0: " + arguments[0]);
//        System.out.println("Char at 0: " + arguments[0].charAt(0));
        char commandID = arguments[0].charAt(0);
        for (BaseCommand command : commands) {
//            System.out.println("Comparing " + commandID + " with " + command.getIdentifier());
            if (commandID == command.getIdentifier()) {
                return command;
            }
        }

        return null;
    }
}
