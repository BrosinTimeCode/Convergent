package com.brosintime.rts.Controller;

import com.brosintime.rts.Commands.Attack;
import com.brosintime.rts.Commands.Command;
import com.brosintime.rts.Commands.CommandList;
import com.brosintime.rts.Commands.Help;
import com.brosintime.rts.Commands.Move;
import com.brosintime.rts.Commands.Select;
import com.brosintime.rts.Log.PageBook;
import com.brosintime.rts.Log.UserLog;
import com.brosintime.rts.Log.UserLogItem;
import com.brosintime.rts.Log.UserLogItem.Type;
import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.TestBoard;
import com.brosintime.rts.Model.TestBoard.BoardType;
import com.brosintime.rts.Units.BaseUnit;
import com.brosintime.rts.View.CommandLineInterface;
import com.brosintime.rts.View.GameViewInterface;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

/**
 * The GameController class follows the controller design in the MVC design pattern. This class
 * receives input from a view controlled by a user and manipulates the board model accordingly.
 */
public class GameController {

    GameViewInterface viewInterface;
    Board board;
    private static BaseUnit player1SelectedUnit;
    private final HashMap<BaseUnit, BaseUnit> entitiesUnderAttack;
    private final UserInputHistory inputHistory;

    public GameController(int viewType, BoardType boardType, int width, int height) {
        int rows = height;
        int columns = width;
        // Command line arguments give size of board. No arguments results in height and width of board being random from 0 to 30
        if (rows == 0 && columns == 0) {
            Random randomGenerator = new Random();
            rows = randomGenerator.nextInt(30) + 1;
            columns = randomGenerator.nextInt(30) + 1;
        }
        if (boardType != BoardType.EMPTY) {
            board = new TestBoard(boardType, rows, columns);
        } else {
            board = new Board(rows, columns);
        }
        switch (viewType) {
            default -> {
                viewInterface = new CommandLineInterface(board);
                viewInterface.initialize();
            }
        }
        CommandList.initializeCommands();
        entitiesUnderAttack = new HashMap<>();
        inputHistory = new UserInputHistory();
        viewInterface.displayHelp();
    }

    /**
     * Initializes the RefreshMapTask and DamageEntityTask to run every one second.
     */
    public void initialize() {
        Timer timer = new Timer();
        long oneSecond = 1000;
        RefreshMapTask task = new RefreshMapTask(viewInterface, board);
        timer.schedule(task, 0, oneSecond);
        DamageEntityTask damageTask = new DamageEntityTask(this);
        timer.schedule(damageTask, 0, oneSecond);
    }

    public void getUserInput() {
        List<Character> input = new ArrayList<>();
        viewInterface.clearInput();
        boolean inputClosed = false;
        do {
            viewInterface.displayInput(charListToString(input));
            KeyStroke keyStroke = viewInterface.getUserKeyStroke();
            switch (keyStroke.getKeyType()) {
                case Escape -> {
                    input.clear();
                    viewInterface.clearInput();
                    inputClosed = true;
                }
                case Character -> {
                    if (input.size() < 80) {
                        input.add(keyStroke.getCharacter());
                    }
                }
                case Enter -> {
                    this.inputHistory.add(charListToString(input));
                    handleUserInput(charListToString(input));
                    input.clear();
                    inputClosed = true;
                }
                case Backspace -> {
                    if (input.size() >= 1) {
                        input.remove(input.size() - 1);
                    }
                    viewInterface.clearInput();
                }
                case ArrowDown -> {
                    input.clear();
                    viewInterface.clearInput();
                    input.addAll(stringToCharList(this.inputHistory.next()));
                }
                case ArrowUp -> {
                    input.clear();
                    viewInterface.clearInput();
                    input.addAll(stringToCharList(this.inputHistory.previous()));
                }
            }
        } while (!inputClosed);
    }

    public void handleUserInput(String input) {
        Command userCommand = CommandList.getCommandFromInput(input);
        if (userCommand == null) {
            viewInterface.displayInvalidCommand();
        } else {
            executeCommand(userCommand);
        }
    }

    private String charListToString(List<Character> list) {
        if (list.size() == 0) {
            return "";
        }
        return list.toString().substring(1, 3 * list.size() - 1).replaceAll(", ", "");
    }

    private List<Character> stringToCharList(String string) {
        List<Character> list = new ArrayList<>();
        for (char c : string.toCharArray()) {
            list.add(c);
        }
        return list;
    }

    private boolean executeCommand(Command command) {
        if (command instanceof Move) {
            return executeMove((Move) command);
        } else if (command instanceof Select) {
            return executeSelect((Select) command);
        } else if (command instanceof Attack) {
            return executeAttack((Attack) command);
        } else if (command instanceof Help) {
            return executeHelp((Help) command);
        }
        return false;
    }


    /**
     * Executes passed in move command. Depending on the state of the arguments it will return false
     * if move command did not execute successfully.
     *
     * @param moveCommand A move command to be executed.
     * @return A boolean showing if the move command executed successfully.
     */
    private boolean executeMove(Move moveCommand) {
        // TODO: make move remove attacked entity in damaged entities hash map
        switch (moveCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, general info is printed
                Command command = CommandList.getCommandFromAlias(moveCommand.getDefaultAlias());
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
                if (arguments.size() == 3) {
                    player1SelectedUnit = board.getUnit(Integer.parseInt(arguments.get(0)));
                    return moveUnit(Integer.parseInt(arguments.get(1)),
                      Integer.parseInt(arguments.get(2)));
                }
                if (arguments.size() == 1) {
                    UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                      "Executing move command...", Type.INFO));
                    viewInterface.displayConsoleLog();
                    return moveToUnit(Integer.parseInt(arguments.get(0)));
                } else if (arguments.size() == 2) {
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

    /**
     * Moves currently selected unit to row and column in the board. If currently selected unit is
     * attacking it will no longer be attacking.
     *
     * @param column An integer representing destination column.
     * @param row    An integer representing destination row.
     * @return A boolean showing if the unit was successfully moved.
     */
    private boolean moveUnit(int column, int row) {
        if (player1SelectedUnit == null) {
            return false;
        }
        if (checkBounds(row, column)) {
            entitiesUnderAttack.remove(player1SelectedUnit);
            board.moveUnit(player1SelectedUnit, row, column);
            return true;
        }
        return false;
    }

    /**
     * Moves currently selected unit to the unit with the passed in id. If currently selected unit
     * is attacking it will no longer be attacking.
     *
     * @param id An integer representing the id of the unit currently selected unit is moving to.
     * @return A boolean showing if the unit was successfully moved.
     */
    private boolean moveToUnit(int id) {
        if (player1SelectedUnit == null) {
            return false;
        }
        entitiesUnderAttack.remove(player1SelectedUnit);
        board.moveToUnit(player1SelectedUnit, id);
        return true;
    }

    /**
     * Executes passed in attack command. Depending on the state of the arguments it will return
     * false if move command did not execute successfully.
     *
     * @param attackCommand An attack command to be executed.
     * @return A boolean showing if the attack command executed successfully.
     */
    private boolean executeAttack(Attack attackCommand) {
        switch (attackCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, general info is printed
                Command command = CommandList.getCommandFromAlias(attackCommand.getDefaultAlias());
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

    /**
     * Attacks unit at row and column on board with currently selected unit.
     *
     * @param column An integer representing the column of location to be attacked.
     * @param row    An integer representing the row of location to be attacked.
     * @return A boolean showing if the unit successfully attacked the square on the board.
     */
    private boolean attackUnit(int column, int row) {
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

    /**
     * Kills deadUnit and removes attacker from entities under attack.
     *
     * @param attacker BaseUnit that is attacking deadUnit.
     * @param deadUnit BaseUnit to be killed.
     */
    public void killUnit(BaseUnit attacker, BaseUnit deadUnit) {
        board.killUnit(deadUnit.getId());
        entitiesUnderAttack.remove(attacker);
    }

    /**
     * Executes passed in select command. Depending on the state of the arguments it will return
     * false if select command did not execute successfully. If no arguments are passed currently
     * selected unit is deselected.
     *
     * @param selectCommand An attack command to be executed.
     * @return A boolean showing if the select command executed successfully.
     */
    private boolean executeSelect(Select selectCommand) {
        switch (selectCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, the currently selected unit is deselected
                player1SelectedUnit = null;
                UserLog.add(
                  new UserLogItem(TextColor.ANSI.CYAN_BRIGHT, "Deselected unit", Type.INFO));
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> { // arguments are parsable as positive integers
                List<String> arguments = new ArrayList<>(selectCommand.getArguments());
                if (arguments.size() == 1) {
                    player1SelectedUnit = board.getUnit(Integer.parseInt(arguments.get(0)));
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

    private boolean executeHelp(Help helpCommand) {
        switch (helpCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, a list of commands is printed
                List<UserLogItem> aliasesList = new ArrayList<>();
                for (Map.Entry<String, Command> entry : CommandList.ALIASES.entrySet()) {
                    aliasesList.add(
                      new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, entry.getKey(), Type.INFO));
                }
                UserLog.add(
                  PageBook.paginateAndGetPage("List of commands", helpCommand.getDefaultAlias(),
                    viewInterface.getConsoleLogHeight(), aliasesList, 1));
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> { // if user asked for a page number or a command alias that was found
                if (CommandList.isAnAlias(helpCommand.getArgument(0))) {
                    Command command = CommandList.getCommandFromAlias(helpCommand.getArgument(0));
                    UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                      command.getName() + " - " + command.getDescription() + " Usages:",
                      Type.INFO));
                    HashMap<Integer, String> usages = new HashMap<>(command.getUsages());
                    usages.forEach((key, value) -> UserLog.add(
                      new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                        command.getDefaultAlias() + " " + value, Type.INFO)));
                    viewInterface.displayConsoleLog();
                    return true;
                } else {
                    List<UserLogItem> aliasesList = new ArrayList<>();
                    for (Map.Entry<String, Command> entry : CommandList.ALIASES.entrySet()) {
                        aliasesList.add(
                          new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, entry.getKey(),
                            Type.INFO));
                    }
                    PageBook.paginateAndGetPage("List of commands", helpCommand.getDefaultAlias(),
                      viewInterface.getConsoleLogHeight(),
                      aliasesList, Integer.parseInt(helpCommand.getArgument(0)));
                    viewInterface.displayConsoleLog();
                    return true;
                }
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

    /**
     * Selects visible unit at row and column on board. If row or column out of bounds select will
     * fail.
     *
     * @param column An integer representing the column for a unit to be selected.
     * @param row    An integer representing the row for a unit to be selected.
     * @return A boolean showing if unit was successfully selected.
     */
    private boolean selectUnit(int column, int row) {
        if (checkBounds(row, column)) {
            player1SelectedUnit = board.getUnit(row, column);
            return true;
        }
        return false;
    }

    /**
     * Checks if row and column are within the bounds of the board.
     *
     * @param row    An integer representing the row to check bounds for.
     * @param column An integer representing the row to check bounds for.
     * @return Returns true if the row and column are in bounds.
     */
    private boolean checkBounds(int row, int column) {
        return !(row > board.getBoardHeight() || row < 0 || column > board.getBoardWidth()
          || column < 0);
    }

    public HashMap<BaseUnit, BaseUnit> getEntitiesUnderAttack() {
        return entitiesUnderAttack;
    }

    public static boolean isPlayer1SelectedUnit(BaseUnit unit) {
        return player1SelectedUnit == unit;
    }
}
