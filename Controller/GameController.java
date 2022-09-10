package Controller;

import Commands.Attack;
import Commands.Command;
import Commands.CommandList;
import Commands.Help;
import Commands.Attack;
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

import java.util.*;

public class GameController {

    GameViewInterface viewInterface;
    Board board;
    private BaseUnit player1SelectedUnit;
    private final HashMap<BaseUnit, BaseUnit> entitiesUnderAttack;

    public GameController(int viewType, int[] boardSize) {
        if (boardSize.length != 2) {
            Random randomGenerator = new Random();
            int rows = randomGenerator.nextInt(20) + 1;
            int columns = randomGenerator.nextInt(20) + 1;
            board = new TestBoard(rows, columns);
        } else {
            board = new Board(boardSize[0], boardSize[1]);
        }
        switch (viewType) {
            default -> {
                viewInterface = new CommandLineInterface(board);
                viewInterface.initialize();
            }
        }
        CommandList.initializeCommands();
        entitiesUnderAttack = new HashMap<>();
    }

    public void initialize() {
        Timer timer = new Timer();
        long oneSecond = 1000;
        RefreshMapTask task = new RefreshMapTask(viewInterface, board);
        timer.schedule(task, 0, oneSecond);
        DamageEntityTask damageTask = new DamageEntityTask(this);
        timer.schedule(damageTask, 0, oneSecond);
    }

    public void handleUserInput() {
        viewInterface.displayHelp();
        while (true) {
            String userInput = viewInterface.getUserInput();
            Command userCommand = CommandList.getCommandFromAlias(userInput);
            if (userCommand == null) {
                viewInterface.displayInvalidCommand();
            } else {
                executeCommand(userCommand);
            }
        }
    }

    public boolean executeCommand(Command command) {
        if (command instanceof Move) {
            return executeMove((Move) command);
        } else if (command instanceof Select) {
            return executeSelect((Select) command);
        } else if (command instanceof Attack) {
            return executeAttack((Attack) command);
        } else if (command instanceof Help) {
            return executeHelp((Help) command);
        } else if (command instanceof Attack) {
            return executeAttack((Attack) command);
        }
        return false;
    }

    // TODO: make move remove attacked entity in damaged entities hash map
    public boolean executeMove(Move moveCommand) {
        switch (moveCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, general info is printed
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
            case GOOD -> { // arguments are parsable as positive integers
                List<String> arguments = new ArrayList<>(moveCommand.getArguments());
                if (arguments.size() == 1) {
                    return true;
                } else {
                    UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                            "Executing move command...", Type.INFO));
                    viewInterface.displayConsoleLog();
                    return moveUnit(Integer.parseInt(arguments.get(0)),
                            Integer.parseInt(arguments.get(1)));
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + moveCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case BAD -> { // arguments are not parsable as positive integers
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Bad syntax! Make sure arguments are numbers", Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
        }
        return false;
    }

    public boolean moveUnit(int row, int column) {
        if (player1SelectedUnit == null) {
            return false;
        }
        if (checkBounds(row, column)) {
            entitiesUnderAttack.remove(player1SelectedUnit);
            board.moveUnit(player1SelectedUnit, row, column);
            return true;
            
    public boolean executeAttack(Attack attackCommand) {
        switch (attackCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, general info is printed
                Command command = CommandList.getCommand(attackCommand.getDefaultAlias());
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
            case GOOD -> { // arguments are parsable as positive integers
                // TODO: Replace the following "Executing attack command" info with actual command
                List<String> arguments = new ArrayList<>(attackCommand.getArguments());
                if (arguments.size() == 1 | arguments.size() == 3) {
                    return true;
                } else {
                    return attackUnit(Integer.parseInt(arguments.get(0)),
                      Integer.parseInt(arguments.get(1)));
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + attackCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case BAD -> { // arguments are not parsable as positive integers
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Bad syntax! Make sure arguments are numbers", Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
        }
        return false;
    }

    public boolean attackUnit(int row, int column) {
        // TODO: Fix for more than one player
        if (player1SelectedUnit == null) {
            return false;
        }
        if (checkBounds(row, column)) {
            entitiesUnderAttack.put(player1SelectedUnit, board.getUnit(row, column));
            return true;
        }
        return false;
    }

    public void killUnit(BaseUnit attacker, BaseUnit deadUnit) {
        board.killUnit(deadUnit.getId());
        entitiesUnderAttack.remove(attacker);
    }

    public boolean executeSelect(Select selectCommand) {
        switch (selectCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, the currently selected unit is deselected
                player1SelectedUnit = null;
                return true;
            }
            case GOOD -> { // arguments are parsable as positive integers
                List<String> arguments = new ArrayList<>(selectCommand.getArguments());
                if (arguments.size() == 1) {
                    return true;
                } else {
                    return selectUnit(Integer.parseInt(arguments.get(0)),
                      Integer.parseInt(arguments.get(1)));
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                  "Too many arguments! Usage: " + selectCommand.getBasicUsage(), Type.INFO));
                viewInterface.displayConsoleLog();
                return false;
            }
            case BAD -> { // arguments aren't parsable as positive integers
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
            case NOARGS -> { // with no arguments, a list of commands is printed
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
            case TOOMANY -> { // too many arguments given
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

    public HashMap<BaseUnit, BaseUnit> getEntitiesUnderAttack() {
        return entitiesUnderAttack;
    }
}
