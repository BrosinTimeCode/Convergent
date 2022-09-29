package com.brosintime.rts.View;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.Units.Unit;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * The CommandLineRenderer class is an object that handles input/output with a terminal window. It
 * has two layers: the bottom layer, which is a render layer that interacts directly with the
 * terminal window and can only be modified by the second layer, which is a screen malleable by
 * methods like {@link #putCell}.
 * <p>On each call of {@link #flush()}, the screen is optimized, with common cells between the
 * screen and the render layer removed. The screen is then flushed to the render layer, which is
 * then rendered to the terminal window.
 * <p>A string representation of both layers is available from {@link #toString()} and is updated
 * as each layer is updated.
 */
public class CommandLineRenderer {

    private final int width;
    private final int height;
    private final Map<Node, Cell> render = new HashMap<>();
    private final Map<Node, Cell> screen = new HashMap<>();
    private Terminal terminal;
    private final StringBuilder[] renderAsString;
    private final StringBuilder[] screenAsString;

    /**
     * Constructs a new CommandLineRender object with the provided width and height. If either
     * dimension is less than one, the respective dimension is set to one.
     *
     * @param width  the width of the terminal in cells
     * @param height the height of the terminal in cells
     */
    public CommandLineRenderer(int width, int height) {
        this.width = Math.max(width, 1);
        this.height = Math.max(height, 1);
        this.renderAsString = new StringBuilder[this.height];
        this.screenAsString = new StringBuilder[this.height];

        for (int row = 0; row < this.height; row++) {
            this.renderAsString[row] = new StringBuilder(this.width + 2)
                .append(" ".repeat(this.width))
                .append("\n");
            this.screenAsString[row] = new StringBuilder(this.width + 2)
                .append(" ".repeat(this.width))
                .append("\n");
        }

        fillRenderWithBlank();

        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            defaultTerminalFactory.setTerminalEmulatorTitle("RTS Game");
            TerminalSize terminalSize = new TerminalSize(this.width, this.height);
            defaultTerminalFactory.setInitialTerminalSize(terminalSize);
            this.terminal = defaultTerminalFactory.createTerminal();
            this.terminal.enterPrivateMode();
            this.terminal.clearScreen();
            this.terminal.setCursorVisible(false);
            this.terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void optimizeScreen() {
        Set<Node> screenKeys = new HashSet<>(this.screen.keySet());
        for (Node node : screenKeys) {
            if (this.screen.get(node).equals(this.render.get(node))) {
                this.screen.remove(node);
            }
        }
    }

    /**
     * Optimizes the screen layer and flushes it to the render layer.
     * <p>The screen layer is optimized by comparing it to the render layer and only keeping cells
     * that are different.
     */
    public void flush() {
        optimizeScreen();
        try {
            Set<Node> screenKeys = new HashSet<>(this.screen.keySet());
            for (Node node : screenKeys) {
                Cell commandLineCell = this.screen.get(node);
                this.screen.remove(node);
                this.screenAsString[node.row()]
                    .replace(node.column(), node.column() + 1, " ");
                placeInTerminal(node, commandLineCell);
                this.render.put(node, commandLineCell);
                this.renderAsString[node.row()]
                    .replace(node.column(), node.column() + 1,
                        Character.toString(commandLineCell.character()));
            }
            this.terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void placeInTerminal(Node node, Cell cell) throws IOException {
        if (this.terminal == null) {
            return;
        }
        this.terminal.setCursorPosition(node.column(), node.row());
        this.terminal.setBackgroundColor(cell.backgroundColor());
        this.terminal.setForegroundColor(cell.foregroundColor());
        this.terminal.putCharacter(cell.character());
    }

    /**
     * Clears the terminal screen and flushes the render layer to the terminal.
     */
    public void refresh() {
        try {
            this.terminal.clearScreen();
            Set<Node> gridKeys = new HashSet<>(this.render.keySet());
            for (Node node : gridKeys) {
                Cell cell = this.render.get(node);
                if (!cell.isBlank()) {
                    placeInTerminal(node, cell);
                }
            }
            this.terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the terminal screen, the render layer, and the screen layer.
     */
    public void clear() {
        try {
            this.terminal.clearScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.screen.clear();
        fillRenderWithBlank();
    }

    /**
     * Draws a line of blank cells to the screen layer.
     *
     * @param row the index of row to draw on
     */
    public void blankLine(int row) {
        row = constrictToBounds(row, this.height - 1);
        Cell blank = Cell.blank();
        for (int x = 0; x < this.width; x++) {
            putCell(blank, x, row);
        }
    }

    private void fillRenderWithBlank() {
        Cell blank = Cell.blank();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.render.put(new Node(y, x), blank);
            }
        }
    }

    /**
     * Replaces a cell in the screen layer by the requested column and row. Out-of-bounds columns
     * and rows are fitted to the screen (i.e. a negative x is fitted to x=0, and an x exceeding
     * screen width is fitted to the last column).
     *
     * @param cell the cell to place
     * @param x    the index of column to place
     * @param y    the index of row to place
     */
    public void putCell(Cell cell, int x, int y) {
        if (cell == null) {
            cell = Cell.blank();
        }
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        this.screen.put(new Node(y, x), cell);
        this.screenAsString[y]
            .replace(x, x + 1, Character.toString(cell.character()));
    }

    /**
     * Retrieves the cell in the render layer by the requested column and row. Out-of-bounds columns
     * and rows are fitted to the screen (i.e. a negative x is fitted to x=0, and an x exceeding
     * screen width is fitted to the last column).
     * <p>If this method has trouble finding the cell, a blank cell is retrieved instead.
     *
     * @param x the index of column to retrieve
     * @param y the index of row to retrieve
     * @return the cell located, or blank if not
     */
    public Cell getCell(int x, int y) {
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        Cell cell = this.render.get(new Node(y, x));
        if (cell == null) {
            cell = Cell.blank();
        }
        return cell;
    }

    /**
     * The width of the renderer in columns. The screen and render layers share the same width.
     *
     * @return the width of the renderer in columns
     */
    public int width() {
        return this.width;
    }

    /**
     * The height of the renderer in rows. The screen and render layers share the same height.
     *
     * @return the height of the renderer in rows
     */
    public int height() {
        return this.height;
    }

    /**
     * Puts a board in the screen layer starting with the board’s first cell (0, 0) placed at the
     * requested (x, y) position. Existing cells in the screen are replaced.
     * <p>Out-of-bounds columns and rows are fitted to the screen (i.e. a negative x is fitted to
     * x=0, and an x exceeding screen width is fitted to the last column). If any board cells spill
     * out-of-bounds of the renderer dimensions, they are not added to the screen at all.
     *
     * @param board the board to print to the screen
     * @param x     the index of column to start
     * @param y     the index of row to start
     */
    public void putBoard(@NotNull Board board, int x, int y) {
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        for (int column = 0; column < board.width(); column++) {
            for (int row = 0; row < board.height(); row++) {
                Unit unit = board.getUnit(row, column);
                if (unit != null) {
                    putCell(unit.toCell(), column * 2 + x, row + y);
                } else {
                    putCell(Cell.blank(), column * 2 + x, row + y);
                }
            }
        }
    }

    /**
     * Puts a string in the screen layer starting from (x, y). The start position is fitted to the
     * screen bounds, but if the string extends past the screen bounds, it is cropped at the edge of
     * the screen and this method stops.
     *
     * @param string          the string to print to the screen
     * @param foregroundColor the color of the string text
     * @param backgroundColor the color underneath the string text
     * @param x               the index of column to start
     * @param y               the index of row to start
     */
    public void putString(String string, TextColor foregroundColor, TextColor backgroundColor,
        int x,
        int y) {
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        for (int c = 0; c < string.length(); c++) {
            if (x + c > this.width - 1) {
                break;
            }
            putCell(new CommandLineCell(foregroundColor, backgroundColor, string.charAt(c)), x + c,
                y);
        }
    }

    /**
     * Draws a rectangle from point A (fitted to screen bounds) to point B (can be outside of screen
     * bounds) to the screen layer, moving in the positive x & y direction. <p>If either dimension
     * is zero-width or less, nothing is drawn. Fill cells are only drawn if the box is large enough
     * (i.e. one or both dimensions are at least 3 cells wide). Rectangles extending past the screen
     * bounds are cropped at the screen edges.
     *
     * @param border the cell to tile as the border, or blank if null
     * @param fill   the cell to file as the fill, or blank if null
     * @param from   point A (inclusive)
     * @param to     point B (exclusive)
     */
    public void drawRectangle(Cell border, Cell fill, Node from, Node to) {
        if (border == null) {
            border = Cell.blank();
        }
        if (fill == null) {
            fill = Cell.blank();
        }
        int deltaX = to.column() - from.column();
        int deltaY = to.row() - from.row();
        if (deltaX <= 0 || deltaY <= 0) {
            return;
        }
        int x = constrictToBounds(from.column(), this.width - 1);
        int y = constrictToBounds(from.row(), this.height - 1);
        for (int row = 0; row < deltaY; row++) {

            boolean inLastRow = row + y + 1 == this.height;
            boolean drawingFirstRow = row == 0;
            boolean drawingLastRow = row == deltaY - 1;

            for (int column = 0; column < deltaX; column++) {

                boolean inLastColumn = column + x + 1 == this.width;
                boolean drawingFirstColumn = column == 0;
                boolean drawingLastColumn = column == deltaX - 1;

                if (!drawingFirstRow && !drawingLastRow && !drawingFirstColumn
                    && !drawingLastColumn) {
                    putCell(fill, column + x, row + y);
                } else {
                    putCell(border, column + x, row + y);
                }

                if (inLastColumn) {
                    break;
                }
            }
            if (inLastRow) {
                break;
            }
        }
    }

    /**
     * Draws a rectangle from point A (fitted to screen bounds) to point B (can be outside of screen
     * bounds) to the screen layer, moving in the positive x & y direction. Nothing inside the
     * rectangle is modified.
     * <p>If either dimension is zero-width or less, nothing is drawn. Rectangles extending past
     * the screen bounds are cropped at the screen edges.
     *
     * @param border the cell to tile as the border, or blank if null
     * @param from   point A (inclusive)
     * @param to     point B (exclusive)
     */
    public void drawRectangleOutline(Cell border, Node from, Node to) {
        if (border == null) {
            border = Cell.blank();
        }
        int deltaX = to.column() - from.column();
        int deltaY = to.row() - from.row();
        if (deltaX <= 0 || deltaY <= 0) {
            return;
        }
        int x = constrictToBounds(from.column(), this.width - 1);
        int y = constrictToBounds(from.row(), this.height - 1);
        for (int row = 0; row < deltaY; row++) {

            boolean inLastRow = row + y + 1 == this.height;
            boolean drawingFirstRow = row == 0;
            boolean drawingLastRow = row == deltaY - 1;

            for (int column = 0; column < deltaX; column++) {

                boolean inLastColumn = column + x + 1 == this.width;
                boolean drawingFirstColumn = column == 0;
                boolean drawingLastColumn = column == deltaX - 1;

                if (drawingFirstRow || drawingLastRow || drawingFirstColumn || drawingLastColumn) {
                    putCell(border, column + x, row + y);
                }

                if (inLastColumn) {
                    break;
                }
            }
            if (inLastRow) {
                break;
            }
        }
    }

    private int constrictToBounds(int number, int upperBound) {
        number = Math.max(number, 0);
        number = Math.min(number, upperBound);
        return number;
    }

    public KeyStroke readInput() throws IOException {
        return this.terminal.readInput();
    }

    public KeyStroke pollInput() throws IOException {
        return this.terminal.pollInput();
    }

    /**
     * String representation of both the screen and render layers of {@link CommandLineRenderer} for
     * debugging. Only {@link Cell#character()}s are used without any representation for colors.
     * Space ‘ ’ characters represent empty cells.
     *
     * @return string representing first the screen layer, followed by the render layer
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
            .append("Screen layer:\n");
        for (StringBuilder line : screenAsString) {
            builder.append(line);
        }
        builder.append("Render layer:\n");
        for (StringBuilder line : renderAsString) {
            builder.append(line);
        }
        return builder.toString();
    }
}
