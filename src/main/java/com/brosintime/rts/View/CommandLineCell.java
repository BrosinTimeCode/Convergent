package View;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import java.util.Objects;

public class CommandLineCell {

    private final TextColor foregroundColor;
    private final TextColor backgroundColor;
    private final char character;

    public CommandLineCell(TextColor foregroundColor, TextColor backgroundColor, char character) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.character = character;
    }

    public static CommandLineCell newBlank() {
        return new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
    }

    public TextColor getForegroundColor() {
        return this.foregroundColor;
    }

    public TextColor getBackgroundColor() {
        return this.backgroundColor;
    }

    public char getCharacter() {
        return this.character;
    }

    @Override
    public String toString() {
        return this.foregroundColor + " " + this.character + " on " + this.backgroundColor;
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
    public int hashCode() {
        return Objects.hash(foregroundColor, backgroundColor, character);
    }
}
