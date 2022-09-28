package com.brosintime.rts.View;

import com.brosintime.rts.Log.UserLog;
import com.brosintime.rts.Log.UserLogItem;
import com.brosintime.rts.Log.UserLogItem.Type;
import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Node;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.IOException;

public class CommandLineInterface implements GameViewInterface {

    private final Board board;
    private int logHeight;
    private final Node boardPosition;
    private final Node logPosition;
    private final Node inputPosition;
    private final Node borderFrom;
    private final Node borderTo;
    private final CommandLineRenderer renderer;

    public CommandLineInterface(Board board) {

        this.board = board;
        this.logHeight = 10;
        this.boardPosition = new Node(4, 5);
        this.logPosition = new Node(board.height() + 6, 2);
        this.inputPosition = new Node(logPosition.row() + logHeight + 1, logPosition.column());
        this.borderFrom = new Node(boardPosition.row() - 1, boardPosition.column() - 1);
        this.borderTo = new Node(borderFrom.row() + board.height() + 2,
            borderFrom.column() + board.width() * 2 + 1);

        this.renderer = new CommandLineRenderer(this.inputPosition.column() + 83,
            this.inputPosition.row() + 2);

    }

    public void displayBoard() {
        this.renderer.putBoard(this.board, this.boardPosition.column(), this.boardPosition.row());

        this.renderer.drawRectangleOutline(
            new CommandLineCell(ANSI.BLACK, ANSI.BLACK_BRIGHT, ' '),
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
                this.renderer.putCell(
                    new CommandLineCell(
                        ANSI.DEFAULT, ANSI.DEFAULT,
                        (char) (xCoordinate / Math.pow(10, digit - 1) % 10 + '0')),
                    boardPosition.column() + xCoordinate * 2,
                    boardPosition.row() - digit - 1
                );
            }
        }

        for (int digit = 0; digit < this.board.width() / 10; digit++) {
            for (int x = 0; x < this.board.width(); x++) {
                int single = (int) (x % Math.pow(10, digit + 1));
                if (single != 0) {
                    this.renderer.putString(
                        String.valueOf(single),
                        ANSI.WHITE, ANSI.BLACK,
                        this.boardPosition.column() + x * 2,
                        this.boardPosition.row() - 2 - digit
                    );
                }
            }
        }

        this.renderer.flush();

    }

    public void displayHelp() {
        UserLogItem log = new UserLogItem(TextColor.ANSI.YELLOW,
            "Type \"m\" to move a unit or \"h\" for a list of commands.", Type.INFO);
        UserLog.add(log);
        displayConsoleLog();
    }

    public void displayInvalidCommand() {
        UserLogItem log = new UserLogItem(TextColor.ANSI.RED,
            "Invalid command! Type \"h\" for a list of commands.", Type.INFO);
        UserLog.add(log);
        displayConsoleLog();
    }

    @Override
    public KeyStroke getUserKeyStroke() {
        try {
            return this.renderer.readInput();
        } catch (IOException e) {
            e.printStackTrace();
            return new KeyStroke(KeyType.Unknown);
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
                UserLog.LOGS.get(UserLog.LOGS.size() - i - 1).getMemo(),
                UserLog.LOGS.get(UserLog.LOGS.size() - i - 1).getColor(),
                ANSI.DEFAULT,
                x, y + 9 - i
            );
        }
        this.renderer.flush();
    }

    @Override
    public void displayInput(String input) {
        this.renderer.putString(
            "/" + input,
            ANSI.DEFAULT, ANSI.DEFAULT,
            this.inputPosition.column(), this.inputPosition.row()
        );
        this.renderer.flush();
    }

    @Override
    public void clearInput() {
        this.renderer.blankLine(this.inputPosition.row());
        this.renderer.flush();
    }

    public void setConsoleLogHeight(int height) {
        this.logHeight = height;
    }

    @Override
    public int getConsoleLogHeight() {
        return this.logHeight;

    }
}
