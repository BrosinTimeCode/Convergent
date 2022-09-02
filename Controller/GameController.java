package Controller;

import Commands.BaseCommand;
import Commands.Move;
import Commands.Select;
import Units.BaseUnit;
import View.GameViewInterface;
import View.CommandLineInterface;
import Model.TestBoard;
import Model.Board;
import java.util.Timer;

public class GameController {

    GameViewInterface viewInterface;
    Board board;
    private BaseUnit player1SelectedUnit;

    public GameController(int viewType, int[] boardSize) {
        if (boardSize.length != 2) {
            board = new TestBoard();
        } else {
            board = new Board(boardSize[0], boardSize[1]);
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
        if (command instanceof Move) {
            return executeMove((Move) command);
        } else if (command instanceof Select) {
            return executeSelect((Select) command);
        }
        return false;
    }

    public boolean executeMove(Move moveCommand) {
        return true;
    }

    public boolean executeSelect(Select selectCommand) {
        String[] arguments = selectCommand.getArguments();
        return selectUnit(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
    }

    public boolean selectUnit(int row, int column) {
        if (checkBounds(row, column)) {
            player1SelectedUnit = board.getUnit(row, column);
            return true;
        }
        return false;
    }

    private boolean checkBounds(int row, int column) {
        return !(row > board.getBoardHeight() || row < 0 || column > board.getBoardWidth()
          || column < 0);
    }
}
