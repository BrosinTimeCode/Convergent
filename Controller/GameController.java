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
        switch (moveCommand.validateArguments()) {
            case NOARGS -> {
                Command command = CommandList.getCommand(moveCommand.getDefaultAlias());
                UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                  command.getName() + " - " + command.getDescription() + " Usages:",
                  Type.INFO));
                HashMap<Integer, String> usages = new HashMap<>(command.getUsages());
                usages.forEach((key, value) -> UserLog.add(
                  new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                    command.getDefaultAlias() + " " + value, Type.INFO)));
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> {
                UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                  "Executing move command...", Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case TOOMANY -> {
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + moveCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case BAD -> {
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Bad syntax! Make sure arguments are numbers", Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
        }
        return false;
    }

    public boolean executeSelect(Select selectCommand) {
        switch (selectCommand.validateArguments()) {
            case NOARGS -> { // if the user deselects the selected unit
                player1SelectedUnit = null;
                return true;
            }
            case GOOD -> { // if all arguments are good
                List<String> arguments = new ArrayList<>(selectCommand.getArguments());
                if (arguments.size() == 1) {
                    return true;
                } else {
                    return selectUnit(Integer.parseInt(arguments.get(0)),
                      Integer.parseInt(arguments.get(1)));
                }
            }
            case TOOMANY -> { // if the user gave too many arguments
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + selectCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case BAD -> { // if any given arguments aren't integers
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Bad syntax! Make sure arguments are numbers", Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
        }
        return false;
    }

    public boolean executeHelp(Help helpCommand) {
        switch (helpCommand.validateArguments()) {
            case NOARGS -> { // if the user asked for a list of commands
                for (Map.Entry<String, Command> entry : CommandList.ALIASES.entrySet()) {
                    UserLog.add(
                      new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, entry.getKey(), Type.INFO));
                }
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> { // if it found the command the user requested help for
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
            }
            case TOOMANY -> { // if the user gave too many arguments
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + helpCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case BAD -> { // if the command alias was not found
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Command not found! Check spelling or type \"help\" for a list of commands",
                  Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
        }
        return false;
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
