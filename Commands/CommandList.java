package Controller;

import Commands.*;

import java.util.ArrayList;

    private static final TreeSet<Alias> aliases = new TreeSet<>();
    private static final HashSet<Command> commands = new HashSet<>();

    public static BaseCommand getCommand(String input) {
        String[] inArguments = input.split("\\s+");
        String[] outArguments = new String[inArguments.length - 1];
        System.arraycopy(inArguments, 1, outArguments, 0, inArguments.length - 1);

        ArrayList<BaseCommand> commands = new ArrayList<>();
        commands.add(new Attack(outArguments));
        commands.add(new Help(outArguments));
        commands.add(new Move(outArguments));
        commands.add(new Select(outArguments));

        try {
            char commandID = inArguments[0].charAt(0);
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

    public static void registerAlias(Alias alias) {
        aliases.add(alias);
    }
}
