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
 * receives input from a view controlled by a player and manipulates the game model accordingly.
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

    /**
     * Constructs a new controller instance. A terminal game window is automatically created with a
     * title screen, but the game does not start until {@link #run()} is called.
     */
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
     * Initializes the main game loop. On each cycle, the player’s key-press pool is processed via
     * {@link GameView#processPlayerKeys()}, then the model is manipulated by {@link #update()},
     * which is then rendered as a frame to the game screen by {@link GameView#renderScreen()}.
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
     * Calls methods that manipulate the game model, like unit movement or damage. This method is
     * called once on each game loop cycle.
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
     * Moves a player’s selected units to the provided coordinates on the board. If the coordinates
     * are out-of-bounds or the player currently has no units selected, the appropriate message is
     * sent to the player instead.
     *
     * @param player the player
     * @param column the destination x coordinate as a positive integer
     * @param row    the destination y coordinate as a positive integer
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
     * Moves a player’s selected units to the location of the unit with the provided ID. If the unit
     * ID is invalid or the player currently has no units selected, the appropriate message is sent
     * to the player instead.
     *
     * @param player the player
     * @param id     the ID of the destination unit as a positive integer
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
     * Executes a move from the network. By default, as a destination, only the x and y coordinates
     * are considered, unless they are both set to -1; only which then the target ID is used
     * instead.
     *
     * @param unitID   the ID of the unit to move as an integer
     * @param targetID the ID of the destination unit as an integer
     * @param x        the destination x coordinate as an integer
     * @param y        the destination y coordinate as an integer
     */
    public void receiveMove(int unitID, int targetID, int x, int y) {
        if (x == -1 && y == -1) {
            board.moveToUnit(board.getUnit(unitID), targetID);
        } else {
            board.moveUnit(board.getUnit(unitID), y, x);
        }
    }

    /**
     * Initiates all the player’s selected units to attack a unit located on the board with the
     * provided coordinates. If the coordinates are out-of-bounds or the player currently has no
     * units selected, the appropriate message is sent to the player instead.
     *
     * @param player the player
     * @param column the target’s x coordinate as a positive integer
     * @param row    the target’s y coordinate as a positive integer
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
     * Initiates all the player’s selected units to attack a unit with the provided ID. If the unit
     * ID is invalid or the player currently has no units selected, the appropriate message is sent
     * to the player instead.
     *
     * @param player the player
     * @param id     the ID of the target unit as a positive integer
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
     * Eliminates the provided dead unit and removes the provided attacker from entities currently
     * under attack.
     *
     * @param attacker the unit attacking the dying unit
     * @param deadUnit the dying unit to be eliminated
     */
    public void killUnit(Unit attacker, Unit deadUnit) {
        board.killUnit(deadUnit.id());
        entitiesUnderAttack.remove(attacker);
    }

    /**
     * Adds a unit with the provided ID to the player’s pool of currently selected units. If the ID
     * is invalid, the appropriate message is sent to the player instead.
     *
     * @param player the player
     * @param ID     the ID of the unit as a positive integer
     */
    public void selectUnit(Player player, int ID) {
        Unit selection = board.getUnit(ID);
        if (selection != null) {
            this.selectedUnits.get(player).add(selection);
            selection.select();
        }
    }

    /**
     * Adds a unit located on the provided coordinates to the player’s pool of currently selected
     * units. If the coordinates are out of bounds, the appropriate message is sent to the player
     * instead.
     *
     * @param player the player
     * @param column the x coordinate location of the unit as a positive integer
     * @param row    the y coordinate location of the unit as a positive integer
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
     * Removes a unit with the provided ID from the player’s pool of currently selected units. If
     * the unit ID is invalid, the appropriate message is sent to the player instead.
     *
     * @param player the player
     * @param ID     the unit’s ID to be deselected as a positive integer
     */
    public void deselectUnit(Player player, int ID) {
        Unit selection = board.getUnit(ID);
        if (selection != null) {
            this.selectedUnits.get(player).remove(selection);
            selection.deselect();
        }
    }

    /**
     * Removes a unit located on the provided coordinates from the player’s pool of currently
     * selected units. If the coordinates are out-of-bounds, the appropriate message is sent to the
     * player instead.
     *
     * @param player the player
     * @param column the x coordinate location of the deselected unit as a positive integer
     * @param row    the y coordinate location of the deselected unit as a positive integer
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
     * Clears the player’s pool of currently selected units.
     *
     * @param player the player
     */
    public void deselectAll(Player player) {
        for (Unit unit : this.selectedUnits.get(player)) {
            unit.deselect();
        }
        this.selectedUnits.get(player).clear();
    }

    /**
     * Checks the boundaries of the board and determines if the provided x and y coordinates are
     * within bounds.
     *
     * @param row    the x coordinate to check as an integer
     * @param column the y coordinate to check as an integer
     * @return {@code true} if both coordinates are in-bounds, or {@code false} if at least one is
     * not
     */
    private boolean checkBounds(int row, int column) {
        return !(row > board.height() - 1 || row < 0 || column > board.width() - 1 || column < 0);
    }

    /**
     * Retrieves the map representing entities currently under attack.
     *
     * @return entities under attack as a {@link Map}
     */
    public Map<Unit, Unit> entitiesUnderAttack() {
        return this.entitiesUnderAttack;
    }


    /**
     * Sends a message to the network. If client is null, nothing happens.
     *
     * @param message the network message to be sent
     */
    private void sendMessage(NetworkMessage message) {
        if (networkClient != null) {
            networkClient.sendMessage(message);
        }
    }

    /**
     * Terminates the game loop after the current cycle finishes.
     */
    public void exit() {
        this.running = false;
    }

    /**
     * Retrieves the current game board.
     *
     * @return the board
     */
    public Board board() {
        return this.board;
    }

    /**
     * Launches a new skirmish game against a CPU with the provided board type.
     *
     * @param boardType the board type to be generated
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
     * Registers a player’s chat log with the game engine. After a chat log is registered, system
     * messages can be sent to specific or all players. The player is also registered to the game if
     * it’s not already.
     *
     * @param player  the player
     * @param chatLog the player’s chat log
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
     * Executes an attack command from a player after it has been parsed by the {@link ChatScreen}.
     *
     * @param player  the commanding player
     * @param command the attack command
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
     * Executes a select command from a player after it has been parsed by the {@link ChatScreen}.
     *
     * @param player  the commanding player
     * @param command the select command
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
     * Executes a move command from a player after it has been parsed by the {@link ChatScreen}.
     *
     * @param player  the commanding player
     * @param command the move command
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
