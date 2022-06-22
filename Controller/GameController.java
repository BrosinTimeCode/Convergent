package Controller;
import View.GameViewInterface;
import View.CommandLineInterface;
import Model.BaseBoard;
import Model.TestBoard;
import Model.Board;
import java.util.Scanner;
import java.util.Timer;

public class GameController {
    GameViewInterface viewInterface;
    BaseBoard board;

    public GameController(int viewType, int boardType) {
        switch(boardType) {
            case 1:
                board = new TestBoard();
                break;
            default:
                board = new Board();
        }
        switch(viewType) {
            default:
                viewInterface = new CommandLineInterface();
        }
    }
    public void initialize() {
        Timer timer = new Timer();
        long oneSecond = 10000;
        RefreshMapTask task = new RefreshMapTask(viewInterface, board);
        timer.schedule(task, 0, oneSecond);
    }
    public void handleUserInput() {
        viewInterface.displayHelp();
        boolean consoleIsOpen = true;
        Scanner userInput = new Scanner(System.in);
        Command command = new Command();
        while (consoleIsOpen) {
            String[] splitCommands = Console.parse(userInput.nextLine());
            char action = command.getAction(splitCommands);
            // If action is not found in command list
            if (action == '#') {
                viewInterface.displayInvalidCommand();
                continue;
            }
            String argsCheck = command.checkArgs(splitCommands, action);
            if (argsCheck != "good") {
                viewInterface.displayCommandError(argsCheck);
                continue;
            }
        }
    }
}
