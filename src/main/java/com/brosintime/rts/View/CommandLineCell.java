package com.brosintime.rts.View;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

/**
 * The CommandLineCell class is the object used
 *
 * @param foregroundColor
 * @param backgroundColor
 * @param character
 */
public record CommandLineCell(TextColor foregroundColor, TextColor backgroundColor,
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
        CommandLineCell that = (CommandLineCell) o;
        return character == that.character && foregroundColor.equals(that.foregroundColor)
            && backgroundColor.equals(that.backgroundColor);
    }

    @Override
    public Cell toCell() {
        return this;
    }
}
