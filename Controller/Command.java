package Controller;

public class Command {
    public char getAction(String[] command) {
        switch (command[0]) {
            case "m":
                return 'm';
            default:
                return '#';
        }
    }
}
