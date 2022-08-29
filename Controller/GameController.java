package Controller;

import Commands.BaseCommand;
import Commands.Move;
import Commands.Select;
import View.GameViewInterface;
import View.CommandLineInterface;
import Model.BaseBoard;
import Model.TestBoard;
import Model.Board;
import java.util.Timer;

public class GameController {

    GameViewInterface viewInterface;
    BaseBoard board;

    public GameController(int viewType, int boardType) {
        switch (boardType) {
            case 1:
                board = new TestBoard();
                break;
            default:
                board = new Board();
        }
        switch (viewType) {
            default:
                viewInterface = new CommandLineInterface(board);
                viewInterface.initialize();
        }
    }

    public void initialize() {
        Timer timer = new Timer();
        long oneSecond = 1000;
        RefreshMapTask task = new RefreshMapTask(viewInterface, board);
        timer.schedule(task, 0, oneSecond);
    }

    public void handleUserInput() {
        viewInterface.displayHelp();
        while (true) {
            String userInput = viewInterface.getUserInput();
            BaseCommand userCommand = Parser.getCommand(userInput);
            if (userCommand == null) {
                viewInterface.displayInvalidCommand();
            }
        }
    }

    public boolean executeCommand(BaseCommand command) {
        if(command instanceof Move) {
            return executeMove((Move) command);
        }
        else if(command instanceof Select) {
            return executeSelect((Select) command);
        }
        return false;
    }

    public boolean executeMove(Move moveCommand) {
        return true;
    }

    public boolean executeSelect(Select selectCommand) {
        String[] arguments = selectCommand.getArguments();
        return board.selectUnit(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
    }
}
