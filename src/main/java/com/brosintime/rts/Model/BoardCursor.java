package com.brosintime.rts.Model;

import com.brosintime.rts.Model.Frame.PlayerCursor;
import com.brosintime.rts.View.Cell;
import com.brosintime.rts.View.TerminalCell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a cursor on a board object, specifically able to be added to board cells. This class
 * is instance-controlled and is limited one-to-one to players.
 */
public class BoardCursor implements PlayerCursor, Cell {

    private final Player player;

    private static final Map<Player, BoardCursor> cursors = new HashMap<>();
    private int row;
    private int column;
    private boolean highlighted = false;

    private BoardCursor(Player player) {
        this.player = player;
        this.row = 0;
        this.column = 0;
    }

    /**
     * Creates a cursor to be owned by the provided player. If the cursor already exists, the
     * existing instance is returned instead.
     *
     * @param player the player that owns the new/retrieved cursor
     * @return the cursor
     */
    public static BoardCursor fromPlayer(Player player) {
        if (cursors.containsKey(player)) {
            return cursors.get(player);
        } else {
            BoardCursor cursor = new BoardCursor(player);
            cursors.put(player, cursor);
            return cursor;
        }
    }

    @Override
    public Player player() {
        return this.player;
    }

    /**
     * Retrieves the row in the board that this cursor exists in.
     *
     * @return the row
     */
    public int row() {
        return this.row;
    }

    /**
     * Sets a new row number for the cursor. This should only be called when the cursor is moved
     * from one board cell to another.
     *
     * @param row the new row it should exist in
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Retrieves the column in the board that this cursor exists in.
     *
     * @return the column
     */
    public int column() {
        return this.column;
    }

    /**
     * Sets a new column number for the cursor. This should only be called when the cursor is moved
     * from one board cell to another.
     *
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public Cell toCell() {
        return new TerminalCell(foregroundColor(), backgroundColor(), character());
    }

    @Override
    public TextColor foregroundColor() {
        return ANSI.DEFAULT;
    }

    @Override
    public TextColor backgroundColor() {
        if (highlighted) {
            switch (this.player.team()) {
                case RED -> {
                    return ANSI.RED;
                }
                case BLUE -> {
                    return ANSI.BLUE;
                }
            }
        } else {
            return ANSI.BLACK_BRIGHT;
        }
        return ANSI.DEFAULT;
    }

    /**
     * Marks this cursor as “highlighted” and changes its cell background color to a highly visible
     * color.
     */
    public void highlight() {
        this.highlighted = true;
    }

    /**
     * Marks this cursor as not “highlighted” and therefore changes its cell background color to a
     * dull color.
     */
    public void unhighlight() {
        this.highlighted = false;
    }

    @Override
    public char character() {
        return ' ';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardCursor cursor = (BoardCursor) o;
        return Objects.equals(player, cursor.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

}
