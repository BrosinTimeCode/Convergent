package com.brosintime.rts.Controller;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Commands.Attack;
import com.brosintime.rts.Model.Commands.CommandList;
import com.brosintime.rts.Model.Commands.Exit;
import com.brosintime.rts.Model.Commands.Help;
import com.brosintime.rts.Model.Commands.Move;
import com.brosintime.rts.Model.Commands.Select;
import com.brosintime.rts.Model.Log.ChatLog;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.Model.Player.Team;
import com.brosintime.rts.Model.TestBoard;
import com.brosintime.rts.Model.TestBoard.BoardType;
import com.brosintime.rts.Model.Units.Unit;
import com.brosintime.rts.Server.Client;
import com.brosintime.rts.Server.NetworkMessages.MoveMessage;
import com.brosintime.rts.Server.NetworkMessages.NetworkMessage;
import com.brosintime.rts.Server.PeerToPeerHost;
import com.brosintime.rts.View.GameView;
import com.brosintime.rts.View.Screen.ChatScreen;
import com.brosintime.rts.View.Screen.Drawable;
import com.brosintime.rts.View.Screen.Drawable.ColorCode;
import com.brosintime.rts.View.Screen.GameScreen;
import com.brosintime.rts.View.TerminalClient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The GameController class follows the controller design in the MVC design pattern. This class
 * receives input from a view controlled by a user and manipulates the board model accordingly.
 */
public class GameController {

    private final GameView client;
    private final Map<String, Integer> debugInfo;
    private final Set<Player> players = new HashSet<>();
    private final Map<Player, ChatLog> chatLogs = new HashMap<>();
    private final Map<Player, Set<Unit>> selectedUnits = new HashMap<>();
    private final Map<Unit, Unit> entitiesUnderAttack = new HashMap<>();
    private final Client networkClient;
    private Board board;
    private boolean running;

    public GameController() {

        Player player1 = new Player(UUID.randomUUID(), "Player1", Team.RED);
        this.players.add(player1);
        this.client = new TerminalClient(this, player1, 192, 48);
        this.debugInfo = new HashMap<>();
        this.debugInfo.put("tps", 0);
        this.debugInfo.put("fps", 0);
        this.networkClient = new PeerToPeerHost();
        this.networkClient.setController(this);

        CommandList.registerCommand(Attack.instance());
        CommandList.registerCommand(Help.instance());
        CommandList.registerCommand(Move.instance());
        CommandList.registerCommand(Select.instance());
        CommandList.registerCommand(Exit.instance());

        this.client.titleScreen();
    }

    /**
     * Runs through the main game loop. On each iteration, player keys are processed, the model is
     * updated, and the game view is rendered.
     */
    public void run() {
        this.running = true;
        long initialTime = System.nanoTime();
        double UPDATES_PER_SECOND = 30;
        final double updateTime = 1000000000 / UPDATES_PER_SECOND;
        double FRAMES_PER_SECOND = 60;
        final double renderTime = 1000000000 / FRAMES_PER_SECOND;
        double updateDelta = 0;
        double renderDelta = 0;
        long timer = System.currentTimeMillis();

        while (this.running) {
            long currentTime = System.nanoTime();
            updateDelta += (currentTime - initialTime) / updateTime;
            renderDelta += (currentTime - initialTime) / renderTime;
            initialTime = currentTime;

            if (updateDelta >= 1) {
                this.client.processPlayerKeys();
                this.update();
                this.debugInfo.put("tps", this.debugInfo.get("tps") + 1);
                updateDelta--;
            }

            if (renderDelta >= 1) {
                this.client.renderScreen();
                this.debugInfo.put("fps", this.debugInfo.get("fps") + 1);
                renderDelta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                if (this.client.debugScreen() != null) {
                    this.client.debugScreen().renderDebugInfo(this.debugInfo);
                }
                this.debugInfo.put("fps", 0);
                this.debugInfo.put("tps", 0);
                timer += 1000;
            }
        }
    }

    /**
     * Updates the model based on processes in progress, like units firing on each other or units
     * being trained.
     */
    private void update() {
        entitiesUnderAttack().forEach((k, v) -> {
            boolean deadUnit = v.damageEntity(k);
            if (deadUnit) {
                killUnit(k, v);
            }
        });
    }

    /**
     * Renders the game view frame and flushes it to the screen.
     */
    public void moveSelected(Player player, int column, int row) {
        Set<Unit> selected = this.selectedUnits.get(player);
        if (selected.isEmpty()) {
            this.chatLogs.get(player).error("Nothing is selected");
            return;
        }
        if (checkBounds(row, column)) {
            for (Unit unit : this.selectedUnits.get(player)) {
                this.entitiesUnderAttack.remove(unit);
                board.moveUnit(unit, row, column);
            }
        } else {
            this.chatLogs.get(player).error("Target coordinates are out of bounds");
        }
    }

    /**
     * Interfaces with {@link GameView} by calling {@link GameView#getPlayerKey()} and processing
     * the keystrokes. If the player submits a line, {@link #handleUserInput} is called to retrieve
     * a command.
     * <p>Player submissions are logged via {@link PlayerInputHistory} and accessible with the up
     * and down arrow keys.
     */
    public void moveSelected(Player player, int id) {
        Set<Unit> selected = this.selectedUnits.get(player);
        if (selected.isEmpty()) {
            this.chatLogs.get(player).error("Nothing is selected");
            return;
        }
        for (Unit unit : this.selectedUnits.get(player)) {
            entitiesUnderAttack.remove(unit);
            board.moveToUnit(unit, id);
        }
    }

    /**
     * Focuses the board.
     */
    public void receiveMove(int unitID, int targetID, int x, int y) {
        if (x == -1 && y == -1) {
            board.moveToUnit(board.getUnit(unitID), targetID);
        } else {
            board.moveUnit(board.getUnit(unitID), y, x);
        }
    }

    /**
     * Focuses the chat frame.
     */
    public void attackUnit(Player player, int column, int row) {
        if (this.selectedUnits.get(player).isEmpty()) {
            this.chatLogs.get(player).error("Nothing is selected");
        } else if (checkBounds(row, column)) {
            this.chatLogs.get(player).error("Unit ID not found");
            Unit target = board.getUnit(row, column);
            for (Unit unit : this.selectedUnits.get(player)) {
                entitiesUnderAttack.put(unit, target);
            }
        }
    }

    /**
     * Toggles the display of debug statistics.
     */
    public void attackUnit(Player player, int id) {
        if (this.selectedUnits.get(player).isEmpty()) {
            this.chatLogs.get(player).error("Nothing is selected");
        } else if (board.getUnit(id) == null) {
            this.chatLogs.get(player).error("Unit ID not found");
        } else {
            Unit target = board.getUnit(id);
            for (Unit unit : this.selectedUnits.get(player)) {
                entitiesUnderAttack.put(unit, target);
            }
        }
    }

    /**
     * Processes player input and retrieves a {@link Command} if found. If a command is not found,
     * an invalid command error is displayed.
     *
     * @param input by player
     */
    public void killUnit(Unit attacker, Unit deadUnit) {
        board.killUnit(deadUnit.id());
        entitiesUnderAttack.remove(attacker);
    }

    public void selectUnit(Player player, int ID) {
        Unit selection = board.getUnit(ID);
        if (selection != null) {
            this.selectedUnits.get(player).add(selection);
            selection.select();
        }
    }

    /**
     * Executes passed in move command. Depending on the state of the arguments it will return false
     * if move command did not execute successfully.
     *
     * @param moveCommand A move command to be executed.
     * @return A boolean showing if the move command executed successfully.
     */
    public void selectUnit(Player player, int column, int row) {
        if (this.checkBounds(row, column)) {
            Unit selection = board.getUnit(row, column);
            if (selection != null) {
                this.selectedUnits.get(player).add(selection);
                selection.select();
            }
        } else {
            this.chatLogs.get(player)
                .info(ColorCode.DARK_RED.fgColor() + "Coordinates are out of bounds");
        }
    }

    /**
     * Moves currently selected unit to row and column in the board. If currently selected unit is
     * attacking it will no longer be attacking.
     *
     * @param column An integer representing destination column.
     * @param row    An integer representing destination row.
     * @return A boolean showing if the unit was successfully moved.
     */
    public void deselectUnit(Player player, int ID) {
        Unit selection = board.getUnit(ID);
        if (selection != null) {
            this.selectedUnits.get(player).remove(selection);
            selection.deselect();
        }
    }

    /**
     * Moves currently selected unit to the unit with the passed in id. If currently selected unit
     * is attacking it will no longer be attacking.
     *
     * @param id An integer representing the id of the unit currently selected unit is moving to.
     * @return A boolean showing if the unit was successfully moved.
     */
    public void deselectUnit(Player player, int column, int row) {
        if (this.checkBounds(row, column)) {
            Unit selection = board.getUnit(row, column);
            if (selection != null) {
                this.selectedUnits.get(player).remove(selection);
                selection.deselect();
            }
        }
    }

    /**
     * Executes a move from the network.
     *
     * @param unitID   an integer representing the ID of the unit to be moved.
     * @param targetID an integer representing the ID of the unit move to.
     * @param x        an integer representing the x coordinate to move to.
     * @param y        an integer representing the y coordinate to move to.
     */
    public void deselectAll(Player player) {
        for (Unit unit : this.selectedUnits.get(player)) {
            unit.deselect();
        }
        this.selectedUnits.get(player).clear();
    }

    /**
     * Executes passed in attack command. Depending on the state of the arguments it will return
     * false if move command did not execute successfully.
     *
     * @param attackCommand An attack command to be executed.
     * @return A boolean showing if the attack command executed successfully.
     */
    private boolean checkBounds(int row, int column) {
        return !(row > board.height() - 1 || row < 0 || column > board.width() - 1 || column < 0);
    }

    /**
     * Attacks unit at row and column on board with currently selected unit.
     *
     * @param column An integer representing the column of location to be attacked.
     * @param row    An integer representing the row of location to be attacked.
     * @return A boolean showing if the unit successfully attacked the square on the board.
     */
    public Map<Unit, Unit> entitiesUnderAttack() {
        return this.entitiesUnderAttack;
    }


    /**
     * Attacks a unit with a specific id with currently selected unit.
     *
     * @param id An integer representing the id identifying a unit on the board.
     * @return A boolean showing if the unit successfully attacked the unit.
     */
    private void sendMessage(NetworkMessage message) {
        if (networkClient != null) {
            networkClient.sendMessage(message);
        }
    }

    /**
     * Kills deadUnit and removes attacker from entities under attack.
    public void exit() {
        this.running = false;
    }

     *
     * @param attacker BaseUnit that is attacking deadUnit.
     * @param deadUnit BaseUnit to be killed.
     */
    public Board board() {
        return this.board;
    }

    /**
     * Executes passed in select command. Depending on the state of the arguments it will return
     * false if select command did not execute successfully. If no arguments are passed currently
     * selected unit is deselected.
     *
     * @param selectCommand An attack command to be executed.
     * @return A boolean showing if the select command executed successfully.
     */
    public void newSkirmish(BoardType boardType) {
        if (boardType == null) {
            throw new IllegalArgumentException("Skirmish cannot start with null board type");
        }
        this.board = new TestBoard(boardType, 10, 10);
        Player cpu = new Player(UUID.randomUUID(), "CPU", Team.BLUE);
        this.players.add(cpu);
        for (Player player : this.players) {
            this.selectedUnits.put(player, new HashSet<>());
        }
        this.board.addPlayers(this.players);
        Drawable gameScreen = new GameScreen(this, this.client);
        this.client.setScreen(gameScreen);
    }

    /**
     * Selects unit on board based on ID. If no such unit select will fail.
     * @param ID An integer representing the id of the unit to select.
     * @return A boolean show if unit was successfully selected.
     */
    public void setChatLog(Player player, ChatLog chatLog) {
        if (player == null) {
            throw new IllegalArgumentException("Cannot register a chat log for a null player");
        }
        if (chatLog == null) {
            throw new IllegalArgumentException("Cannot register a null chat log");
        }
        this.players.add(player);
        this.chatLogs.put(player, chatLog);
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
    public void handleAttack(Player player, Attack command) {
        if (player == null) {
            throw new IllegalArgumentException(
                "Cannot handle attack command because the player is null");
        }
        if (command == null) {
            throw new IllegalArgumentException("Cannot handle attack command that doesn’t exist");
        }
        ChatLog chatLog = this.chatLogs.get(player);
        switch (command.arguments().size()) {
            case 1 -> {
                if (this.board.getUnit(Integer.parseInt(command.argument(0))) == null) {
                    chatLog.error("Target ID is not a valid unit");
                    return;
                }
                if (this.selectedUnits.get(player).size() == 0) {
                    chatLog.error("Nothing is selected");
                    return;
                }
                chatLog.info(ColorCode.CYAN.fgColor() + "Executing attack command...");
                this.attackUnit(player, Integer.parseInt(command.argument(0)));
            }
            case 2 -> {
                chatLog.info(ColorCode.CYAN.fgColor() + "Executing attack command...");
                this.attackUnit(player, Integer.parseInt(command.argument(0)),
                    Integer.parseInt(command.argument(1)));
            }
            case 3 -> {
                chatLog.info(ColorCode.CYAN.fgColor() + "Executing attack command...");
                this.deselectAll(player);
                this.selectUnit(player, Integer.parseInt(command.argument(0)));
                this.attackUnit(player, Integer.parseInt(command.argument(1)), Integer.parseInt(
                    command.argument(2)));
            }
        }
    }

    /**
     * Selects visible unit at row and column on board. If row or column out of bounds select will
     * fail.
     *
     * @param column An integer representing the column for a unit to be selected.
     * @param row    An integer representing the row for a unit to be selected.
     * @return A boolean showing if unit was successfully selected.
     */
    public void handleSelect(Player player, Select command) {
        if (player == null) {
            throw new IllegalArgumentException(
                "Cannot handle select command because the player is null");
        }
        if (command == null) {
            throw new IllegalArgumentException("Cannot handle select command that doesn’t exist");
        }
        if (command.arguments().size() == 1) {
            this.selectUnit(player, Integer.parseInt(command.argument(0)));
        } else {
            this.selectUnit(player, Integer.parseInt(command.argument(0)),
                Integer.parseInt(command.argument(1)));
        }
    }

    /**
     * Checks if row and column are within the bounds of the board.
     *
     * @param row    An integer representing the row to check bounds for.
     * @param column An integer representing the row to check bounds for.
     * @return Returns true if the row and column are in bounds.
     */
    /**
     * Sends a message to the network. If client is null does nothing.
     *
     * @param message Message to send over the network.
     */
    public void handleMove(Player player, Move command) {
        if (player == null) {
            throw new IllegalArgumentException(
                "Cannot handle move command because the player is null");
        }
        if (command == null) {
            throw new IllegalArgumentException("Cannot handle move command that doesn’t exist");
        }
        ChatLog chatLog = this.chatLogs.get(player);
        switch (command.arguments().size()) {
            case 1 -> {
                chatLog.info(ColorCode.CYAN.fgColor() + "Executing move command...");
                for (Unit unit : this.selectedUnits.get(player)) {
                    this.sendMessage(
                        new MoveMessage(unit.id(), Integer.parseInt(command.argument(0)), -1, -1));
                    this.moveSelected(player, Integer.parseInt(command.argument(0)));
                }
            }
            case 2 -> {
                chatLog.info(ColorCode.CYAN.fgColor() + "Executing move command...");
                for (Unit unit : this.selectedUnits.get(player)) {
                    this.sendMessage(
                        new MoveMessage(unit.id(), -1, Integer.parseInt(command.argument(0)),
                            Integer.parseInt(command.argument(1))));
                    this.moveSelected(player, Integer.parseInt(command.argument(0)),
                        Integer.parseInt(
                            command.argument(1)));
                }
            }
            case 3 -> {
                chatLog.info(ColorCode.CYAN.fgColor() + "Executing move command...");
                this.deselectAll(player);
                this.selectUnit(player, Integer.parseInt(command.argument(0)));
                this.sendMessage(
                    new MoveMessage(Integer.parseInt(command.argument(0)), -1, Integer.parseInt(
                        command.argument(1)), Integer.parseInt(command.argument(2))));
                this.moveSelected(player, Integer.parseInt(command.argument(1)), Integer.parseInt(
                    command.argument(2)));
            }
        }
    }
}
