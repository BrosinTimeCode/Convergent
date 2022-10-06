package com.brosintime.rts.View;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

/**
 * Convertible to a {@link TerminalCell}. This interface also has a static method of generating a
 * blank cell, {@link #blank()}.
 */
public interface Cell {

    /**
     * Determines if this cell is <i>blank</i>. A cell is blank if has {@link ANSI#DEFAULT}
     * foreground and background colors and is represented by a space ‘ ’ character.
     *
     * @return true if blank, false if not
     */
    boolean isBlank();

    /**
     * Retrieves a {@link TerminalCell} representation of this object.
     *
     * @return this object as a cell
     */
    Cell toCell();

    /**
     * Retrieves the foreground color of this object as a cell.
     *
     * @return foreground color
     */
    TextColor foregroundColor();

    /**
     * Retrieves the background color of this object as a cell.
     *
     * @return background color
     */
    TextColor backgroundColor();

    /**
     * Retrieves the character representation of this object as a cell.
     *
     * @return character
     */
    char character();

    /**
     * Retrieves a new blank cell instance with default foreground and background colors,
     * represented by a space ‘ ’ character.
     *
     * @return new blank cell
     */
    static TerminalCell blank() {
        return new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
    }


}
