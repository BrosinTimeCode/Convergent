package com.brosintime.rts.View;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

/**
 * The {@code TerminalCell} class represents a cell used to populate the {@link TerminalRenderer}
 * screens. Each cell is represented by a single character, its color, and a background (or cell)
 * color.
 *
 * @param foregroundColor the color of the character
 * @param backgroundColor the color of the background
 * @param character       the character representation
 */
public record TerminalCell(TextColor foregroundColor, TextColor backgroundColor,
                           char character) implements Cell {

    @Override
    public boolean isBlank() {
        return this.foregroundColor == ANSI.DEFAULT && this.backgroundColor == ANSI.DEFAULT
            && this.character == ' ';
    }

    @Override
    public String toString() {
        if (isBlank()) {
            return "BLANKCELL";
        } else {
            return this.foregroundColor + " '" + this.character + "' on "
                + this.backgroundColor;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TerminalCell that = (TerminalCell) o;
        return character == that.character && foregroundColor.equals(that.foregroundColor)
            && backgroundColor.equals(that.backgroundColor);
    }

    @Override
    public Cell toCell() {
        return this;
    }
}
