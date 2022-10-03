package com.brosintime.rts.Model.Frame;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.BoardCursor;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;

public class TerminalBoardFrame extends BoardFrame {

    GameController controller;
    GameView client;
    Board board;
    Player player;
    BoardCursor cursor;

    public TerminalBoardFrame(GameController controller, GameView client, Board board,
        Player player) {
        this.controller = controller;
        this.client = client;
        this.board = board;
        this.player = player;
        this.cursor = this.board.getCursor(this.player);
    }

    @Override
    public void onFocus() {
        this.cursor.highlight();
        // TODO: toggle rendering controls help on
        this.client.clearControls();
        this.client.setControlsString(
            "<Enter> Open chat " +
                "<s> Select unit " +
                "<Space>/<m> Move selected unit\n" +
                "<d> Deselect unit " +
                "<a> Attack unit"
        );
    }

    @Override
    public void offFocus() {
        this.cursor.unhighlight();
        // TODO: toggle rendering controls help off
    }

    @Override
    public void executeKey(KeyStroke key) {
        switch (key.getKeyType()) {
            case Enter -> this.controller.focusChatFrame();
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
                        this.controller.selectUnit(this.cursor.column(), this.cursor.row());
                    }
                    case 'm', ' ' -> {
                        this.controller.moveUnit(this.cursor.column(), this.cursor.row());
                    }
                    case 'd' -> {
                        this.controller.deselectUnit();
                    }
                    case 'a' -> {
                        this.controller.attackUnit(this.cursor.column(), this.cursor.row());
                    }
                }
            }
            case F3 -> this.controller.toggleDebugScreen();
        }
    }

}
