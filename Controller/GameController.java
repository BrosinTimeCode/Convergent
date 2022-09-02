package Controller;

import Commands.Command;
import Commands.CommandList;
import Commands.Help;
import Commands.Move;
import Commands.Select;
import Log.UserLogItem;
import Log.UserLogItem.Type;
import Units.BaseUnit;
import View.GameViewInterface;
import View.CommandLineInterface;
import Model.TestBoard;
import Model.Board;
import com.googlecode.lanterna.TextColor;
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
        CommandList.initializeCommands();
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
            Command userCommand = CommandList.getCommand(userInput);
            if (userCommand == null) {
                viewInterface.displayInvalidCommand();
            } else if (!executeCommand(userCommand)) {
                UserLogItem userLogItem = new UserLogItem(TextColor.ANSI.RED, "Command failed!",
                  Type.INFO);
                viewInterface.log(userLogItem);
            }
        }
    }

    public boolean executeCommand(Command command) {
        if (command instanceof Move) {
            return executeMove((Move) command);
        } else if (command instanceof Select) {
            return executeSelect((Select) command);
        } else if (command instanceof Help) {
            return executeHelp((Help) command);
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

    public boolean executeHelp(Help selectCommand) {
        String[] arguments = selectCommand.getArguments();
        return true;
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
