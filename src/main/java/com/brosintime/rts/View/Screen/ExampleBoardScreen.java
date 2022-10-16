package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Node;
import com.googlecode.lanterna.input.KeyStroke;

/**
 * This class is similar to {@link BoardScreen} in how it is a wrapper for a board, but with the
 * sole purpose of displaying the board.
 */
public class ExampleBoardScreen extends Screen {

    Board board;

    /**
     * Constructs a new display wrapper for a board. If the provided origin Node is null, an origin
     * of (0, 0) is set. An {@link IllegalArgumentException} is thrown if the provided board is
     * null.
     *
     * @param origin the origin Node that determines where the first board cell is placed
     * @param board the board that should be displayed
     */
    public ExampleBoardScreen(Node origin, Board board) {
        this.origin = origin != null ? origin : new Node(0, 0);
        if (board == null) {
            throw new IllegalArgumentException("Cannot draw a null board to a screen");
        }
        this.board = board;
        this.height = this.board.height();
        this.width = this.board.width() * 2;
    }

    @Override
    public void onFocus() {

    }

    @Override
    public void offFocus() {

    }

    @Override
    public void onKeyPress(KeyStroke key) {

    }

    @Override
    public void onRender() {
        this.screen.putAll(Drawable.fromBoard(board, null, this.origin));
    }
}
