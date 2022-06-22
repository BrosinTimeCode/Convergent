package Controller;

public class Command {
    public char getAction(String[] command) {
        switch (command[0]) {
            case "a":
                return 'a';
            case "m":
                return 'm';
            case "s":
                return 's';
            case "h":
                return 'h';
            default:
                return '#';
        }
    }
    public String checkArgs(String[] command, char action) {
        int argsCount = command.length;
        int args[];
        switch (action) {
            case 'a':
                if (argsCount == 1) {
                    return "Attack: Asks a (selected) unit to attack another. Usage: a (unit) [[unit]|[x]] [y]";
                }
                else if (argsCount >= 5) {
                    return "Too many arguments! Usage: a (unit) [[unit]|[x]] [y]";
                }
                else {
                    try {
                        args = new int[argsCount - 1];
                        for (int i = 0; i < argsCount - 1; i++) {
                            args[i] = Integer.parseInt(command[i+1]);
                        }
                    }
                    catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        return "Invalid argument(s)! Usage: a (unit) [[unit]|[x]] [y]";
                    }
                }
                // TODO: attack command
            case 'm':
                if (argsCount == 1) {
                    return "Move: Asks a (selected) unit to move to another unit's current square or a set of coordinates. Usage: m ([unit]|[x]) ([x]|[y]) [y]";
                }
                else if (argsCount >= 5) {
                    return "Too many arguments! Usage: m ([unit]|[x]) ([x]|[y]) [y]";
                }
                else {
                    try {
                        args = new int[argsCount - 1];
                        for (int i = 0; i < argsCount - 1; i++) {
                            args[i] = Integer.parseInt(command[i+1]);
                        }
                    }
                    catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        return "Invalid argument(s)! Usage: m ([unit]|[x]) ([x]|[y]) [y]";
                    }
                }
                // TODO: move command
            case 's':
                if (argsCount >= 4) {
                    return "Too many arguments! Usage: s ([unit]|[x]) [y]";
                }
                else if (argsCount != 1) {
                    try {
                        args = new int[argsCount - 1];
                        for (int i = 0; i < argsCount - 1; i++) {
                            args[i] = Integer.parseInt(command[i+1]);
                        }
                    }
                    catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        return "Invalid argument(s)! Usage: s ([unit]|[x]) [y]";
                    }
                }
                // TODO: select command
            case 'h':
                return "a: Attack something\nm: Move a unit\ns: Select/Deselect\nh: Display help with a list of commands";
            default:
                return "Invalid command! Type \"h\" for a list of commands.";
        }
    }
}
