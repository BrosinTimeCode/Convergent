package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.BoardCursor;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;

/**
 * A {@link Screen} wrapper for a {@link Board}. When in focus, this screen interfaces player
 * key-presses with the board, executing hotkey commands and moving the player’s cursor on the
 * board.
 */
public class BoardScreen extends Screen {

    private final Board board;
    private final Player player;
    private final BoardCursor cursor;
    private final Drawable chatScreen;
    private final Node boardOrigin;

    /**
     * Constructs a new screen wrapper for a board. If the provided controller or client is null, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param controller the game controller to interface with
     * @param client     the game client to interface with
     */
    public BoardScreen(GameController controller, GameView client) {
        if (controller == null) {
            throw new IllegalArgumentException("This screen has no game engine");
        }
        if (client == null) {
            throw new IllegalArgumentException("This screen has no game client");
        }
        this.controller = controller;
        this.client = client;
        this.origin = new Node(0, 0);
        this.board = this.controller.board();
        if (this.board == null) {
            throw new NullPointerException("The game engine doesn’t have a board");
        }
        this.player = this.client.player();
        if (this.player == null) {
            throw new NullPointerException("The game client doesn’t have a player");
        }
        this.cursor = this.board.getCursor(this.player);
        this.chatScreen = new ChatScreen(this.controller, this.client,
            new Node(0, 0)).asChild(this).asHidden();
        this.height = this.client.height();
        this.width = this.client.width();
        this.boardOrigin = Drawable.originCenteredOn(
            Drawable.fromBoard(this.board, this.player, this.origin), this);
    }

    @Override
    public void onFocus() {
        this.cursor.highlight();
    }

    @Override
    public void offFocus() {
        this.cursor.unhighlight();
    }

    @Override
    public void onKeyPress(KeyStroke key) {
        switch (key.getKeyType()) {
            case Enter -> this.client.setScreen(this.chatScreen);
            case ArrowUp ->
                this.board.moveCursor(this.player, this.cursor.column(), this.cursor.row() - 1);
            case ArrowDown ->
                this.board.moveCursor(this.player, this.cursor.column(), this.cursor.row() + 1);
            case ArrowLeft ->
                this.board.moveCursor(this.player, this.cursor.column() - 1, this.cursor.row());
            case ArrowRight ->
                this.board.moveCursor(this.player, this.cursor.column() + 1, this.cursor.row());
            case Character -> {
                switch (key.getCharacter()) {
                    case 's' -> {
                        this.controller.selectUnit(this.player, this.cursor.column(),
                            this.cursor.row());
                    }
                    case 'm', ' ' -> {
                        this.controller.moveSelected(this.player, this.cursor.column(),
                            this.cursor.row());
                    }
                    case 'd' -> {
                        this.controller.deselectAll(this.player);
                    }
                    case 'a' -> {
                        this.controller.attackUnit(this.player, this.cursor.column(),
                            this.cursor.row());
                    }
                }
            }
            case F3 -> this.client.toggleDebugScreen();
        }
    }

    @Override
    public void onRender() {
        this.screen.putAll(Drawable.fromBoard(this.board, this.player, this.boardOrigin));
    }

}
