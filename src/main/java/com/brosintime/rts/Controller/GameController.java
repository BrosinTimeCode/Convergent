package com.brosintime.rts.Controller;

import com.brosintime.rts.Commands.Attack;
import com.brosintime.rts.Commands.Command;
import com.brosintime.rts.Commands.CommandList;
import com.brosintime.rts.Commands.CommandList.NullCommand;
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
import com.brosintime.rts.Units.Unit;
import com.brosintime.rts.View.CommandLineInterface;
import com.brosintime.rts.View.GameViewInterface;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The GameController class follows the controller design in the MVC design pattern. This class
 * receives input from a view controlled by a user and manipulates the board model accordingly.
 */
public class GameController {

    private final double UPDATES_PER_SECOND = 30;
    private final double FRAMES_PER_SECOND = 60;
    private final Map<String, Integer> debugInfo;
    private boolean isRunning = false;
    private boolean debugScreenIsOn = false;
    private boolean toggleDebugScreen = false;
    private final List<Character> userInput = new ArrayList<>();
    GameViewInterface viewInterface;
    Board board;
    private static Unit player1SelectedUnit;
    private final HashMap<Unit, Unit> entitiesUnderAttack;
    private final UserInputHistory inputHistory;

    public GameController(int viewType, BoardType boardType, int width, int height) {
        this.debugInfo = new HashMap<>();
        this.debugInfo.put("tps", 0);
        this.debugInfo.put("fps", 0);
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
            }
        }
        entitiesUnderAttack = new HashMap<>();

        CommandList.registerCommand(Attack.instance());
        CommandList.registerCommand(Help.instance());
        CommandList.registerCommand(Move.instance());
        CommandList.registerCommand(Select.instance());

        inputHistory = new UserInputHistory();

        this.viewInterface.displayHelp();
        run();
    }

    public void run() {
        this.isRunning = true;
        long initialTime = System.nanoTime();
        final double updateTime = 1000000000 / this.UPDATES_PER_SECOND;
        final double renderTime = 1000000000 / this.FRAMES_PER_SECOND;
        double updateDelta = 0;
        double renderDelta = 0;
        long timer = System.currentTimeMillis();

        while (this.isRunning) {
            long currentTime = System.nanoTime();
            updateDelta += (currentTime - initialTime) / updateTime;
            renderDelta += (currentTime - initialTime) / renderTime;
            initialTime = currentTime;

            if (updateDelta >= 1) {
                getUserInput();
                update();
                this.debugInfo.put("tps", this.debugInfo.get("tps") + 1);
                updateDelta--;
            }

            if (renderDelta >= 1) {
                render();
                this.debugInfo.put("fps", this.debugInfo.get("fps") + 1);
                renderDelta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                if (this.debugScreenIsOn) {
                    this.viewInterface.renderDebugScreen(this.debugInfo);
                }
                this.debugInfo.put("fps", 0);
                this.debugInfo.put("tps", 0);
                timer += 1000;
            }
        }
    }

    private void update() {

        getEntitiesUnderAttack().forEach((k, v) -> {
            boolean deadUnit = v.damageEntity(k);
            if (deadUnit) {
                killUnit(k, v);
            }
        });

    }

    private void render() {

        if (this.toggleDebugScreen) {
            this.toggleDebugScreen = false;
            this.debugScreenIsOn = !this.debugScreenIsOn;
            this.viewInterface.clear();
        }

        this.viewInterface.displayBoard();
        this.viewInterface.displayConsoleLog();
        this.viewInterface.clearInput();
        this.viewInterface.displayInput(charListToString(this.userInput));
        this.viewInterface.flush();

    }

    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Interfaces with {@link GameViewInterface} by calling
     * {@link GameViewInterface#getUserKeyStroke()} and processing the keystrokes. If the player
     * submits a line, {@link #handleUserInput} is called to retrieve a command.
     * <p>Player submissions are logged via {@link UserInputHistory} and accessible with the up and
     * down arrow keys.
     */
    public void getUserInput() {
        this.viewInterface.clearInput();
        this.viewInterface.displayInput(charListToString(this.userInput));
        KeyStroke keyStroke = this.viewInterface.getUserKeyStroke();
        if (keyStroke == null) {
            return;
        }
        switch (keyStroke.getKeyType()) {
            case Escape -> {
                this.userInput.clear();
                this.viewInterface.clearInput();
            }
            case Character -> {
                if (this.userInput.size() < 80) {
                    this.userInput.add(keyStroke.getCharacter());
                }
            }
            case Enter -> {
                this.inputHistory.add(charListToString(this.userInput));
                handleUserInput(charListToString(this.userInput));
                this.userInput.clear();
            }
            case Backspace -> {
                if (this.userInput.size() >= 1) {
                    this.userInput.remove(this.userInput.size() - 1);
                }
                this.viewInterface.clearInput();
            }
            case ArrowDown -> {
                this.userInput.clear();
                this.viewInterface.clearInput();
                this.userInput.addAll(stringToCharList(this.inputHistory.next()));
            }
            case ArrowUp -> {
                this.userInput.clear();
                this.viewInterface.clearInput();
                this.userInput.addAll(stringToCharList(this.inputHistory.previous()));
            }
            case F3 -> {
                this.toggleDebugScreen = true;
            }
        }
    }

    /**
     * Processes player input and retrieves a {@link Command} if found. If a command is not found,
     * an invalid command error is displayed.
     *
     * @param input by player
     */
    public void handleUserInput(String input) {
        Command userCommand = CommandList.fromInput(input);
        if (userCommand instanceof NullCommand) {
            viewInterface.displayInvalidCommand();
        } else {
            executeCommand(userCommand);
        }
    }

    /**
     * Converts a {@link List} of {@link Character} to a {@link String}.
     *
     * @param list of {@link Character}
     * @return string output
     */
    private String charListToString(List<Character> list) {
        if (list.size() == 0) {
            return "";
        }
        return list.toString().substring(1, 3 * list.size() - 1).replaceAll(", ", "");
    }

    /**
     * Converts a {@link String} to a {@link List} of {@link Character}.
     *
     * @param string input
     * @return {@link List} of {@link Character}
     */
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
        switch (moveCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, general info is printed
                Command command = CommandList.fromAlias(moveCommand.defaultAlias());
                UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                    command.name() + " - " + command.description() + " Usages:", Type.INFO));
                List<String> usages = new ArrayList<>(command.usages());
                for (String usage : usages) {
                    UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                        command.defaultAlias() + " " + usage, Type.INFO));
                }
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> { // arguments are parsable as positive integers
                List<String> arguments = new ArrayList<>(moveCommand.getArguments());
                if (arguments.size() == 1) {
                    UserLog.add(
                        new UserLogItem(TextColor.ANSI.CYAN_BRIGHT, "Executing move command...",
                            Type.INFO));
                    viewInterface.displayConsoleLog();
                    return moveToUnit(Integer.parseInt(arguments.get(0)));
                } else if (arguments.size() == 2) {
                    if (!checkBounds(Integer.parseInt(arguments.get(1)),
                        Integer.parseInt(arguments.get(0)))) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Move coordinates are out of bounds.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                        "Executing move command...", Type.INFO));
                    viewInterface.displayConsoleLog();
                    return moveUnit(Integer.parseInt(arguments.get(0)),
                        Integer.parseInt(arguments.get(1)));
                } else if (arguments.size() == 3) {
                    if (!checkBounds(Integer.parseInt(arguments.get(2)),
                        Integer.parseInt(arguments.get(1)))) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Move coordinates are out of bounds.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    player1SelectedUnit = board.getUnit(Integer.parseInt(arguments.get(0)));
                    return moveUnit(Integer.parseInt(arguments.get(1)),
                        Integer.parseInt(arguments.get(2)));
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                    "Too many arguments! Usage: " + moveCommand.basicUsage(), Type.INFO));
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
                Command command = CommandList.fromAlias(attackCommand.defaultAlias());
                UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                    command.name() + " - " + command.description() + " Usages:", Type.INFO));
                List<String> usages = new ArrayList<>(command.usages());
                for (String usage : usages) {
                    UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                        command.defaultAlias() + " " + usage, Type.INFO));
                }
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> { // arguments are parsable as positive integers
                List<String> arguments = new ArrayList<>(attackCommand.getArguments());
                if (arguments.size() == 1) {
                    if (board.getUnit(Integer.parseInt(arguments.get(0))) == null) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Target ID is not a valid unit.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    if (player1SelectedUnit == null) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "No attacker selected.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                        "Executing attack command...", Type.INFO));
                    viewInterface.displayConsoleLog();
                    return attackUnitID(Integer.parseInt(arguments.get(0)));
                } else if (arguments.size() == 2) {
                    if (!checkBounds(Integer.parseInt(arguments.get(1)),
                        Integer.parseInt(arguments.get(0)))) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Attacking an area that is out of bounds.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                        "Executing attack command...", Type.INFO));
                    viewInterface.displayConsoleLog();
                    return attackUnit(Integer.parseInt(arguments.get(0)),
                        Integer.parseInt(arguments.get(1)));
                } else if (arguments.size() == 3) {
                    if (!checkBounds(Integer.parseInt(arguments.get(2)),
                        Integer.parseInt(arguments.get(1)))) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Attacking an area that is out of bounds.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    if (board.getUnit(Integer.parseInt(arguments.get(0))) == null) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Selected unit ID is not a valid unit.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    UserLog.add(new UserLogItem(TextColor.ANSI.CYAN_BRIGHT,
                        "Executing attack command...", Type.INFO));
                    viewInterface.displayConsoleLog();
                    player1SelectedUnit = board.getUnit(Integer.parseInt(arguments.get(0)));
                    return attackUnit(Integer.parseInt(arguments.get(1)),
                        Integer.parseInt(arguments.get(2)));
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                    "Too many arguments! Usage: " + attackCommand.basicUsage(), Type.INFO));
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
     * Attacks a unit with a specific id with currently selected unit.
     *
     * @param id An integer representing the id identifying a unit on the board.
     * @return A boolean showing if the unit successfully attacked the unit.
     */
    private boolean attackUnitID(int id) {
        // TODO: Fix for more than one player
        if (player1SelectedUnit == null) {
            return false;
        } else if (board.getUnit(id) == null) {
            return false;
        } else {
            entitiesUnderAttack.put(player1SelectedUnit, board.getUnit(id));
            return true;
        }
    }

    /**
     * Kills deadUnit and removes attacker from entities under attack.
     *
     * @param attacker BaseUnit that is attacking deadUnit.
     * @param deadUnit BaseUnit to be killed.
     */
    public void killUnit(Unit attacker, Unit deadUnit) {
        board.killUnit(deadUnit.id());
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
                    if (!checkBounds(Integer.parseInt(arguments.get(1)),
                        Integer.parseInt(arguments.get(0)))) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                            "Selection coordinates are out of bounds.", Type.INFO));
                        viewInterface.displayConsoleLog();
                        return false;
                    }
                    return selectUnit(Integer.parseInt(arguments.get(0)),
                        Integer.parseInt(arguments.get(1)));
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                    "Too many arguments! Usage: " + selectCommand.basicUsage(), Type.INFO));
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

    /**
     * Executes passed in {@link Help} command. Returns true only if command executes successfully.
     * <p>If no arguments are provided, the console displays a paginated list of command aliases
     * registered in {@link CommandList}. If a valid command alias is provided as the sole argument,
     * help documentation for the appropriate command is displayed. Otherwise, an error is displayed
     * in the console and this method returns false.
     *
     * @param helpCommand instance of {@link Help}
     * @return true if provided valid arguments, otherwise false
     */
    private boolean executeHelp(Help helpCommand) {
        switch (helpCommand.validateArguments()) {
            case NOARGS -> { // with no arguments, a list of commands is printed
                List<UserLogItem> aliasesList = new ArrayList<>();
                for (Map.Entry<String, Command> entry : CommandList.ALIASES.entrySet()) {
                    aliasesList.add(
                        new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, entry.getKey(), Type.INFO));
                }
                UserLog.add(
                    PageBook.paginateAndGetPage("List of commands", helpCommand.defaultAlias(),
                        viewInterface.getConsoleLogHeight(), aliasesList, 1));
                viewInterface.displayConsoleLog();
                return true;
            }
            case GOOD -> { // if user asked for a page number or a command alias that was found
                if (CommandList.isAnAlias(helpCommand.getArgument(0))) {
                    Command command = CommandList.fromAlias(helpCommand.getArgument(0));
                    UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                        command.name() + " - " + command.description() + " Usages:", Type.INFO));
                    List<String> usages = new ArrayList<>(command.usages());
                    for (String usage : usages) {
                        UserLog.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT,
                            command.defaultAlias() + " " + usage, Type.INFO));
                    }
                    viewInterface.displayConsoleLog();
                    return true;
                } else {
                    List<UserLogItem> aliasesList = new ArrayList<>();
                    for (Map.Entry<String, Command> entry : CommandList.ALIASES.entrySet()) {
                        aliasesList.add(
                            new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, entry.getKey(),
                                Type.INFO));
                    }
                    PageBook.paginateAndGetPage("List of commands", helpCommand.defaultAlias(),
                        viewInterface.getConsoleLogHeight(), aliasesList,
                        Integer.parseInt(helpCommand.getArgument(0)));
                    viewInterface.displayConsoleLog();
                    return true;
                }
            }
            case TOOMANY -> { // too many arguments given
                UserLog.add(new UserLogItem(TextColor.ANSI.RED,
                    "Too many arguments! Usage: " + helpCommand.basicUsage(), Type.INFO));
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
            player1SelectedUnit.select();
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
        return !(row > board.height() - 1 || row < 0 || column > board.width() - 1 || column < 0);
    }

    public HashMap<Unit, Unit> getEntitiesUnderAttack() {
        return entitiesUnderAttack;
    }

    public static boolean isPlayer1SelectedUnit(Unit unit) {
        return player1SelectedUnit == unit;
    }
}
