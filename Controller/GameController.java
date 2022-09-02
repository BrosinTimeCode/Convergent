package Controller;

import Commands.Command;
import Commands.CommandList;
import Commands.Help;
import Commands.Move;
import Commands.Select;
import Log.UserLog;
import Log.UserLogItem;
import Log.UserLogItem.Type;
import Units.BaseUnit;
import View.GameViewInterface;
import View.CommandLineInterface;
import Model.TestBoard;
import Model.Board;
import com.googlecode.lanterna.TextColor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Command userCommand = CommandList.getCommandFromInput(userInput);
            if (userCommand == null) {
                viewInterface.displayInvalidCommand();
            } else if (!executeCommand(userCommand)) {
                continue;
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
        List<String> arguments = new ArrayList<>(selectCommand.getArguments());
        return selectUnit(Integer.parseInt(arguments.get(0)), Integer.parseInt(arguments.get(1)));
    }

    public boolean executeHelp(Help helpCommand) {
        switch (helpCommand.validateArguments()) {
            case 0: // if the user asked for a list of commands
                for (Map.Entry<String, Command> entry : CommandList.ALIASES.entrySet()) {
                    UserLog.add(
                      new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, entry.getKey(), Type.INFO));
                }
                viewInterface.displayConsoleLog();
                return true;
            case 1: // if it found the command the user requested help for
                try {
                    Command command = CommandList.getCommand(helpCommand.getArgument(0));
                    UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                      command.getName() + " - " + command.getDescription() + " Usages:",
                      Type.INFO));
                    HashMap<Integer, String> usages = new HashMap<>(command.getUsages());
                    usages.forEach((key, value) -> UserLog.add(
                      new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                        command.getDefaultAlias() + " " + value, Type.INFO)));
                    viewInterface.displayConsoleLog();
                    return true;
                } catch (IndexOutOfBoundsException iob) {
                    UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                      "IndexOutOfBounds exception retrieving Help arguments!",
                      Type.INFO));
                    viewInterface.displayConsoleLog();
                    return false;
                }
            case 99: // if the user gave too many arguments
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + helpCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            case 98: // if the command alias was not found
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Command not found! Check spelling or type \"help\" for a list of commands",
                  Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            default:
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "An error occurred executing the help command!",
                  Type.INFO));
                viewInterface.displayConsoleLog();
                return false;

        }
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
