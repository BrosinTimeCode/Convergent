package com.brosintime.rts.View;

import com.brosintime.rts.Log.UserLog;
import com.brosintime.rts.Log.UserLogItem;
import com.brosintime.rts.Log.UserLogItem.Type;
import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.Model.Player;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;
import java.util.Map;

public class TerminalClient implements GameView {

    private final Board board;
    private final int logHeight;
    private final Node boardPosition;
    private final Node controlsPosition;
    private final Node logPosition;
    private final Node inputPosition;
    private final Node borderFrom;
    private final Node borderTo;
    private final Node debugInfoPosition;
    private final TerminalRenderer renderer;
    public final StringBuilder controls;

    public TerminalClient(Player player, Board board) {

        this.board = board;
        this.logHeight = 10;
        this.boardPosition = new Node(4, 5);
        this.controlsPosition = new Node(board.height() + 6, 2);
        this.logPosition = new Node(controlsPosition.row() + 3, 2);
        this.inputPosition = new Node(logPosition.row() + logHeight + 1, logPosition.column());
        this.borderFrom = new Node(boardPosition.row() - 1, boardPosition.column() - 1);
        this.borderTo = new Node(borderFrom.row() + board.height() + 2,
            borderFrom.column() + board.width() * 2 + 1);
        this.debugInfoPosition = new Node(inputPosition.row() + 2, 0);
        this.controls = new StringBuilder();

        this.renderer = new TerminalRenderer(
            this.inputPosition.column() + 83,
            this.debugInfoPosition.row() + 1,
            player
        );

    }

    @Override
    public void displayBoard() {
        this.renderer.putBoard(this.board, this.boardPosition.column(), this.boardPosition.row());

        this.renderer.drawRectangleOutline(
            new TerminalCell(ANSI.BLACK, ANSI.BLACK_BRIGHT, ' '),
            this.borderFrom, this.borderTo
        );

        for (int y = 0, yDigits; y < this.board.height(); y++) {
            yDigits = String.valueOf(y).length();
            this.renderer.putString(
                String.valueOf(y),
                ANSI.DEFAULT, ANSI.DEFAULT,
                this.boardPosition.column() - yDigits - 1,
                this.boardPosition.row() + y
            );
        }

        for (int xCoordinate = 0; xCoordinate < board.width(); xCoordinate++) {
            int digits = Integer.toString(xCoordinate).length();
            for (int digit = 1; digit <= digits; digit++) {
                char c = (char) (xCoordinate / Math.pow(10, digit - 1) % 10 + '0');
                this.renderer.putCell(
                    new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, c),
                    boardPosition.column() + xCoordinate * 2,
                    boardPosition.row() - digit - 1
                );
            }
        }

    }

    @Override
    public void displayInvalidCommand() {
        UserLogItem log = new UserLogItem(TextColor.ANSI.RED,
            "Invalid command! Type \"h\" for a list of commands.", Type.INFO);
        UserLog.add(log);
        displayConsoleLog();
    }

    @Override
    public KeyStroke getPlayerKey() {
        try {
            return this.renderer.pollInput();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void displayConsoleLog() {
        // TODO: add check for UserLogItem scope (DEV, CHAT, INFO)
        int x = this.logPosition.column();
        int y = this.logPosition.row();
        for (int i = 0; i < this.logHeight; i++) {
            this.renderer.blankLine(y + i);
        }
        for (int i = 0; i < 10 && i < UserLog.LOGS.size(); i++) {
            this.renderer.putString(
                UserLog.LOGS.get(UserLog.LOGS.size() - i - 1).memo(),
                UserLog.LOGS.get(UserLog.LOGS.size() - i - 1).color(),
                ANSI.DEFAULT,
                x, y + 9 - i
            );
        }
    }

    @Override
    public void displayInput(String input) {
        this.renderer.putString(
            "/" + input,
            ANSI.DEFAULT, ANSI.DEFAULT,
            this.inputPosition.column(), this.inputPosition.row()
        );
    }

    @Override
    public void clearInput() {
        this.renderer.blankLine(this.inputPosition.row());
    }

    @Override
    public int getConsoleLogHeight() {
        return this.logHeight;
    }

    @Override
    public void flush() {
        this.renderer.flush();
    }

    @Override
    public void clear() {
        this.renderer.clear();
    }

    @Override
    public void renderDebugScreen(Map<String, Integer> debugInfo) {
        this.renderer.blankLine(this.debugInfoPosition.row());
        this.renderer.putString(
            "FPS: " + debugInfo.get("fps") + " TPS: " + debugInfo.get("tps"),
            ANSI.DEFAULT, ANSI.DEFAULT,
            this.debugInfoPosition.column(), this.debugInfoPosition.row()
        );
    }

    @Override
    public void displayControls() {
        String[] controls = this.controls.toString().split("\n");
        int line = 0;
        for (String str : controls) {
            this.renderer.putString(
                str,
                ANSI.DEFAULT, ANSI.DEFAULT,
                this.controlsPosition.column(), this.controlsPosition.row() + line
            );
            line++;
        }
    }

    @Override
    public void clearControls() {
        this.renderer.blankLine(this.controlsPosition.row());
        this.renderer.blankLine(this.controlsPosition.row() + 1);
    }

    @Override
    public void setControlsString(String string) {
        if (this.controls.length() > 0) {
            this.controls.delete(0, this.controls.length());
        }
        this.controls.append(string);
    }
}
