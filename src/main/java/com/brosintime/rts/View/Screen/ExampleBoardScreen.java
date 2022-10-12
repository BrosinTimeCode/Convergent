package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Node;
import com.googlecode.lanterna.input.KeyStroke;

public class ExampleBoardScreen extends Screen {

    Board board;

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
