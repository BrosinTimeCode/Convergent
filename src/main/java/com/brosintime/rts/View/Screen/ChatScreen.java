package com.brosintime.rts.View.Screen;

import static com.brosintime.rts.Controller.PlayerInputHistory.asCharList;
import static com.brosintime.rts.Controller.PlayerInputHistory.asString;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Controller.PlayerInputHistory;
import com.brosintime.rts.Model.Commands.Attack;
import com.brosintime.rts.Model.Commands.Command;
import com.brosintime.rts.Model.Commands.CommandList;
import com.brosintime.rts.Model.Commands.CommandList.NullCommand;
import com.brosintime.rts.Model.Commands.Exit;
import com.brosintime.rts.Model.Commands.Help;
import com.brosintime.rts.Model.Commands.Move;
import com.brosintime.rts.Model.Commands.Select;
import com.brosintime.rts.Model.Log.ChatLog;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.ArrayList;
import java.util.List;

/**
 * The chat screen keeps a log of messages sent between the player and other players or the system
 * and is responsible for rendering them as a cell map. It also keeps a record of player input as
 * entries the player can cycle through.
 * <p>This class is also primarily responsible for parsing the player’s commands and sending them
 * to the game controller.
 * <p>By default, this screen is hidden when out of focus and unhidden when re-focused and is
 * intended to be a child of another screen.
 */
public class ChatScreen extends Screen {

    private final PlayerInputHistory inputHistory;
    private final List<Character> input;
    private final ChatLog chatLog;
    private final int chatHeight;
    private final int chatWidth;

    /**
     * Constructs a new chat screen. If the provided controller or client is null, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param controller the game controller to interface with
     * @param client     the game client to interface with
     */
    public ChatScreen(GameController controller, GameView client, Node origin) {
        if (controller == null) {
            throw new IllegalArgumentException("This screen has no game engine");
        }
        if (client == null) {
            throw new IllegalArgumentException("This screen has no game client");
        }
        this.controller = controller;
        this.client = client;
        this.origin = origin != null ? origin : new Node(0, 0);
        this.inputHistory = new PlayerInputHistory();
        this.input = new ArrayList<>();
        this.chatLog = new ChatLog(7);
        this.controller.setChatLog(this.client.player(), this.chatLog);
        this.chatWidth = 80;
        this.chatHeight = 10;
        this.width = this.chatWidth;
        this.height = this.chatHeight + 2;
    }

    @Override
    public void onFocus() {
        this.show();
    }

    @Override
    public void offFocus() {
        this.hide();
    }

    @Override
    public void onKeyPress(KeyStroke key) {
        switch (key.getKeyType()) {
            case Escape -> {
                this.input.clear();
                this.client.setScreen(this.parent);
            }
            case Character -> {
                if (this.input.size() < this.chatWidth) {
                    this.input.add(key.getCharacter());
                }
            }
            case Enter -> {
                this.inputHistory.add(asString(this.input));
                this.handlePlayerMessage();
                this.input.clear();
            }
            case Backspace -> {
                if (this.input.size() >= 1) {
                    this.input.remove(this.input.size() - 1);
                }
            }
            case ArrowDown -> {
                this.input.clear();
                this.input.addAll(asCharList(this.inputHistory.next()));
            }
            case ArrowUp -> {
                this.input.clear();
                this.input.addAll(asCharList(this.inputHistory.previous()));
            }
        }
    }

    private void handlePlayerMessage() {
        if (this.input.size() == 0) {
            return;
        }
        if (this.input.get(0) == '/') {
            this.input.remove(0);
            Command userCommand = CommandList.fromInput(asString(this.input));
            if (userCommand instanceof NullCommand) {
                this.chatLog.invalidCommand();
            } else {
                this.processCommand(userCommand);
            }
        } else {
            this.chatLog.chat(this.client.player(), asString(this.input));
        }
    }

    private void processCommand(Command command) {
        if (command instanceof Move) {
            this.processMoveCommand((Move) command);
        } else if (command instanceof Select) {
            this.processSelectCommand((Select) command);
        } else if (command instanceof Attack) {
            this.processAttackCommand((Attack) command);
        } else if (command instanceof Help) {
            this.processHelpCommand((Help) command);
        } else if (command instanceof Exit) {
            this.processExitCommand((Exit) command);
        }
    }

    private void processHelpCommand(Help command) {
        switch (command.validateArguments()) {
            case NOARGS -> this.chatLog.allAliases();
            case GOOD -> {
                switch (command.arguments().size()) {
                    case 1 -> {
                        if (CommandList.isAnAlias(command.argument(0))) {
                            this.chatLog.usagesOf(CommandList.fromAlias(command.argument(0)));
                        } else {
                            this.chatLog.allAliases(Integer.parseInt(command.argument(0)));
                        }
                    }
                    case 2 -> this.chatLog.allAliases(Integer.parseInt(command.argument(1)));
                }
            }
            case TOOMANY -> this.chatLog.tooManyArguments(command);
            case BAD -> this.chatLog.error(
                "Command not found! Check spelling or type “help” for a list of commands");
        }
    }

    private void processAttackCommand(Attack command) {
        switch (command.validateArguments()) {
            case NOARGS -> this.chatLog.commandInfo(command);
            case GOOD -> this.controller.handleAttack(this.client.player(), command);
            case TOOMANY -> this.chatLog.tooManyArguments(command);
            case BAD -> this.chatLog.error("Bad Syntax! Make sure arguments are numbers");
        }
    }

    private void processSelectCommand(Select command) {
        switch (command.validateArguments()) {
            case NOARGS -> this.chatLog.commandInfo(command);
            case GOOD -> this.controller.handleSelect(this.client.player(), command);
            case TOOMANY -> this.chatLog.tooManyArguments(command);
            case BAD -> this.chatLog.error("Bad Syntax! Make sure arguments are numbers");
        }
    }

    private void processMoveCommand(Move command) {
        switch (command.validateArguments()) {
            case NOARGS -> this.chatLog.commandInfo(command);
            case GOOD -> this.controller.handleMove(this.client.player(), command);
            case TOOMANY -> this.chatLog.tooManyArguments(command);
            case BAD -> this.chatLog.error("Bad Syntax! Make sure arguments are numbers");
        }
    }

    private void processExitCommand(Exit command) {
        switch (command.validateArguments()) {
            case NOARGS -> this.controller.exit();
            case TOOMANY -> this.chatLog.tooManyArguments(command);
        }
    }

    private void redrawInput() {
        this.screen.putAll(Drawable.blankRow(this, this.chatHeight));
        this.screen.putAll(Drawable.fromString(
            ColorCode.WHITE.fgColor() + asString(this.input) + (this.input.size() < this.chatWidth
                ? "_" : ""),
            Node.relativeTo(this.origin, this.chatHeight, 0), false));
    }

    private void redrawMessages() {
        for (int row = 0; row < this.chatHeight; row++) {
            this.screen.putAll(Drawable.blankRow(this, this.chatHeight - row - 1));
            if (row < this.chatLog.size()) {
                Node node = Node.relativeTo(this.origin, this.chatHeight - row - 1, 0);
                String string = this.chatLog.get(this.chatLog.size() - row - 1).toString();
                this.screen.putAll(Drawable.fromString(string, node, false));
            }
        }
    }

    @Override
    public void onRender() {
        this.redrawInput();
        this.redrawMessages();
    }
}
