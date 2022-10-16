package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.BoardCursor;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.Model.Node.Bounds;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.Model.Player.Team;
import com.brosintime.rts.Model.Units.Unit;
import com.brosintime.rts.View.Cell;
import com.brosintime.rts.View.TerminalCell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Drawable as a screen to the game client. A screen is a map of cells. Screens can:
 * <ul>
 *     <li>have a parent
 *     <li>have one or more children
 *     <li>be pruned alongside its children at once
 *     <li>be hidden
 *     <li>be unhidden
 * </ul>
 * <p>This interface also provides static methods to convert strings or boards to a screen, draw
 * blank rows or columns to a screen, or even blank a screen out entirely.
 */
public interface Drawable {

    /**
     * Returns this screen as a representation of a cell map.
     *
     * @return this screen as a cell map
     */
    Map<Node, Cell> toCells();

    /**
     * Returns this screen’s parent if it has one.
     *
     * @return the parent of this screen
     */
    Drawable parent();

    /**
     * Returns this screen’s children.
     *
     * @return the list of children of this screen
     */
    List<Drawable> children();

    /**
     * Sets the provided screen as the parent of this screen. The old parent is replaced.
     *
     * @param parent the new parent to assign to this screen
     */
    void setParent(Drawable parent);

    /**
     * Determines if this screen is currently visible.
     *
     * @return {@code true} if this screen is visible, or {@code false} if not
     */
    boolean visible();

    /**
     * Sets this screen as visible.
     */
    void show();

    /**
     * Sets this screen as invisible and sets {@link #justHidden()} to true.
     */
    void hide();

    /**
     * Sets this screen as invisible, sets {@link #justHidden()} to true, and returns itself. This
     * method is intended to be called as this screen is instantiated.
     *
     * @return itself
     */
    Drawable asHidden();

    /**
     * Sets the provided screen as the parent of this screen and returns itself. The old parent is
     * replaced. This method is intended to be called as this screen is instantiated.
     *
     * @param parent the new parent to assign to this screen.
     * @return itself
     */
    Drawable asChild(Drawable parent);

    /**
     * Returns the first ancestor of this screen. The first ancestor is the grand-most parent of
     * this screen that doesn’t have a parent.
     *
     * @return the first ancestor of this screen
     */
    Drawable firstAncestor();

    /**
     * Returns the origin point of this screen. The origin is the top-left-most point of this
     * screen.
     *
     * @return the origin point of this screen
     */
    Node origin();

    /**
     * This method is called every time before this screen is converted to a cell map by
     * {@link #toCells()}. Add code here that should occur right before this screen is rendered.
     */
    void onRender();

    /**
     * This method is called every time this screen is hidden by {@link #hide()}. Generally, this
     * method blanks the screen out by replacing all screen cells with blank cells. It also sets
     * {@link #justHidden()} to {@code false}.
     */
    void onHidden();

    /**
     * Determines if this screen is in a state after {@link #hide()} was called, but before
     * {@link #onHidden()} is called. This method is used by {@link #toCells()} to determine if
     * {@link #onHidden()} should be called.
     *
     * @return {@code true} if {@link #hide()} was called but {@link #onHidden()} has not occurred
     * yet, or false otherwise
     */
    boolean justHidden();

    /**
     * Draws a blank row to a new cell map based on the width of the provided screen and the
     * provided row number, and finally returns the new cell map. The cell map is only composed of a
     * single row of blank cells and is safe to be put into a screen’s existing cell map.
     * <p>The provided screen is used to determine relative positions of the new blank cells and
     * how many to create based on the screen’s width, so it is imperative that the provided
     * screen’s {@link #rows()} is accurate.
     *
     * @param screen the screen to canvas for the new blank cells; is only queried and not modified
     * @param row    the index of the screen row to draw, as a positive integer not greater than the
     *               screen’s width
     * @return a new cell map composed only of blank cells in the row
     */
    static Map<Node, Cell> blankRow(Drawable screen, int row) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen cannot be null");
        }
        if (row < 0) {
            throw new IllegalArgumentException("Row (" + row + ") cannot be negative");
        }
        if (row >= screen.rows()) {
            throw new IllegalArgumentException(
                "Row (" + row + ") cannot be greater than last row (" + (screen.rows() - 1) + ")");
        }
        Node origin = screen.origin();
        if (origin == null) {
            throw new NullPointerException("Origin cannot be null");
        }
        Map<Node, Cell> cells = new HashMap<>();
        for (int i = 0; i < screen.columns(); i++) {
            cells.put(Node.relativeTo(origin, row, i), Cell.blank());
        }
        return cells;
    }

    /**
     * Draws a blank column to a new cell map based on the height of the provided screen and the
     * provided row number, and finally returns the new cell map. The cell map is only composed of a
     * single column of blank cells and is safe to put into a screen’s existing cell map.
     * <p>The provided screen is used to determine relative positions of the new blank cells and
     * how many to create based on the screen’s height, so it is imperative that the provided
     * screen’s {@link #columns()} is accurate.
     *
     * @param screen the screen to canvas for the new blank cells; is only queried and not modified
     * @param column the index of the screen column to draw, as a positive integer not greater than
     *               the screen’s width
     * @return a new cell map composed only of blank cells in the column
     */
    static Map<Node, Cell> blankColumn(Drawable screen, int column) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen cannot be null");
        }
        if (column < 0) {
            throw new IllegalArgumentException("Column (" + column + ") cannot be negative");
        }
        if (column >= screen.columns()) {
            throw new IllegalArgumentException(
                "Column (" + column + ") cannot be greater than column row (" + (screen.columns()
                    - 1) + ")");
        }
        Node origin = screen.origin();
        if (origin == null) {
            throw new NullPointerException("Origin cannot be null");
        }
        Map<Node, Cell> cells = new HashMap<>();
        for (int i = 0; i < screen.columns(); i++) {
            cells.put(Node.relativeTo(origin, i, column), Cell.blank());
        }
        return cells;
    }

    /**
     * Draws an array of blank cells to a new cell map based on the dimensions of the provided
     * screen, and returns the new cell map. The returned cell map can then be used to replace all
     * the screen’s cells to blank, effectively “hiding” it.
     * <p>The provided screen is used to determine relative positions of the new blank cells and
     * how many to create, so it is imperative that both the provided screen’s {@link #rows()} and
     * {@link #columns()} is accurate.
     *
     * @param screen the screen to canvas for the new blank cells; is only queried and not modified
     * @return a new cell map composed of blank cells spanning the full dimensions of the provided
     * screen
     */
    static Map<Node, Cell> blankScreen(Drawable screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen cannot be null");
        }
        Node origin = screen.origin();
        if (origin == null) {
            throw new NullPointerException("Origin cannot be null");
        }
        Map<Node, Cell> cells = new HashMap<>();
        for (int row = 0; row < screen.rows(); row++) {
            for (int column = 0; column < screen.columns(); column++) {
                cells.put(Node.relativeTo(origin, row, column), Cell.blank());
            }
        }
        return cells;
    }

    /**
     * Draws a new cell map copied from the provided cell map, but repositioned so that the
     * horizontal center of the cell map aligns with the center of the provided screen. The new cell
     * map is then returned.
     * <p>The horizontal center is determined by dividing the total horizontal span of the cell
     * map’s node positions in half.
     *
     * @param cells  the cell map to be copied and the copy repositioned; is only queried and not
     *               modified
     * @param screen the screen to center the new cell map on
     * @return a new cell map copied from the provided cell map but repositioned
     */
    static Map<Node, Cell> centerHorizontally(Map<Node, Cell> cells, Screen screen) {
        Set<Node> nodes = new HashSet<>(cells.keySet());
        Map<Bounds, Integer> bounds = Node.bounds(nodes);
        int cellsCenter = (bounds.get(Bounds.MAX_X) - bounds.get(Bounds.MIN_X)) / 2;
        int screenCenter = screen.columns() / 2 - 1;
        int shift = screenCenter - cellsCenter;
        if (shift == 0) {
            return cells;
        }
        Map<Node, Cell> newScreen = new HashMap<>();
        for (Node node : nodes) {
            Cell cell = cells.get(node);
            newScreen.put(new Node(node.row(), node.column() + shift), cell);
        }
        return newScreen;
    }

    /**
     * Draws a new cell map copied from the provided cell map, but repositioned so that the vertical
     * center of the cell map aligns with the center of the provided screen. The new cell map is
     * then returned.
     * <p>The vertical center is determined by dividing the total vertical span of the cell map’s
     * node positions in half.
     *
     * @param cells  the cell map to be copied and the copy repositioned; is only queried and not
     *               modified
     * @param screen the screen to center the new cell map on
     * @return a new cell map copied from the provided cell map but repositioned
     */
    static Map<Node, Cell> centerVertically(Map<Node, Cell> cells, Screen screen) {
        Set<Node> nodes = new HashSet<>(cells.keySet());
        Map<Bounds, Integer> bounds = Node.bounds(nodes);
        int cellsCenter = (bounds.get(Bounds.MAX_Y) - bounds.get(Bounds.MIN_Y)) / 2;
        int screenCenter = screen.rows() / 2 - 1;
        int shift = screenCenter - cellsCenter;
        if (shift == 0) {
            return cells;
        }
        Map<Node, Cell> newScreen = new HashMap<>();
        for (Node node : nodes) {
            Cell cell = cells.get(node);
            newScreen.put(new Node(node.row() + shift, node.column()), cell);
        }
        return newScreen;
    }

    /**
     * Draws a new cell map copied from the provided cell map, but repositioned so that the
     * horizontal and vertical center of the cell map aligns with the center of the provided screen.
     * The new cell map is then returned.
     * <p>The horizontal and vertical centers are determined by dividing the total horizontal or
     * vertical span of the cell map’s node positions in half.
     *
     * @param cells  the cell map to be copied and the copy repositioned; is only queried and not
     *               modified
     * @param screen the screen to center the new cell map on
     * @return a new cell map copied from the provided cell map but repositioned
     */
    static Map<Node, Cell> center(Map<Node, Cell> cells, Screen screen) {
        Node cellsCenter = centerOf(cells);
        Node screenCenter = centerOf(screen);

        Node shift = new Node(
            screenCenter.row() - cellsCenter.row(),
            screenCenter.column() - cellsCenter.column());

        if (shift.row() == 0 && shift.column() == 0) {
            return cells;
        }
        Map<Node, Cell> newScreen = new HashMap<>();
        for (Node node : cells.keySet()) {
            Cell cell = cells.get(node);
            newScreen.put(new Node(
                node.row() + shift.row(), node.column() + shift.column()), cell);
        }
        return newScreen;
    }

    /**
     * Finds a new origin for the provided cell map so that when set, the cell map is centered
     * horizontally on the provided screen.
     *
     * @param cells  the cell map to find a new origin for
     * @param screen the screen to center the cell map on
     * @return a new origin
     */
    static Node originCenteredHorizontallyOn(Map<Node, Cell> cells, Drawable screen) {
        Node offset = offsetFrom(cells, screen);
        return Node.relativeTo(screen.origin(), 0, offset.column());
    }

    /**
     * Finds a new origin for the provided cell map so that when set, the cell map is centered
     * vertically on the provided screen.
     *
     * @param cells  the cell map to find a new origin for
     * @param screen the screen to center the cell map on
     * @return a new origin
     */
    static Node originCenteredVerticallyOn(Map<Node, Cell> cells, Drawable screen) {
        Node offset = offsetFrom(cells, screen);
        return Node.relativeTo(screen.origin(), offset.row(), 0);
    }

    /**
     * Finds a new origin for the provided cell map so that when set, the cell map is centered
     * horizontally and vertically on the provided screen.
     *
     * @param cells  the cell map to find a new origin for
     * @param screen the screen to center the cell map on
     * @return a new origin
     */
    static Node originCenteredOn(Map<Node, Cell> cells, Drawable screen) {
        Node offset = offsetFrom(cells, screen);
        return Node.relativeTo(screen.origin(), offset.row(), offset.column());
    }

    private static Node offsetFrom(Map<Node, Cell> cells, Drawable screen) {
        Node cellMapSpan = spanOf(cells.keySet());
        Node screenSpan = new Node(screen.rows(), screen.columns());
        return new Node((screenSpan.row() - cellMapSpan.row()) / 2,
            (screenSpan.column() - cellMapSpan.column()) / 2);
    }

    private static Node centerOf(Map<Node, Cell> cells) {
        Node span = spanOf(cells.keySet());
        return new Node(span.row() / 2, span.column() / 2);
    }

    private static Node spanOf(Set<Node> nodes) {
        Map<Bounds, Integer> bounds = Node.bounds(nodes);
        return new Node(bounds.get(Bounds.MAX_Y) - bounds.get(Bounds.MIN_Y),
            bounds.get(Bounds.MAX_X) - bounds.get(Bounds.MIN_X));
    }

    private static Node centerOf(Screen screen) {
        // Since a screen’s row count starts at 1 and a node starts at 0, the quotient is
        // subtracted by 1
        return new Node(screen.rows() / 2 - 1, screen.columns() / 2 - 1);
    }

    /**
     * Converts the provided string to a cell map, with the first character at (0, 0) relative to
     * the provided origin. If the string is empty, an empty cell map is returned instead. Passing
     * {@code ignoreSpaces} as {@code true} ignores spaces in the string and therefore does not
     * convert them to cells.
     * <p>Strings can contain {@link ColorCode} characters, which affect the color of the
     * characters or their backgrounds. To ignore color code characters, pass the string in
     * {@link #removeColorCodes} first.
     *
     * @param string       the string to convert to a cell map
     * @param origin       the node location to place the first character
     * @param ignoreSpaces {@code true} skips space characters, {@code false} renders them
     * @return a new cell map representation of the provided string
     */
    static Map<Node, Cell> fromString(String string, Node origin, boolean ignoreSpaces) {
        Map<Node, Cell> screen = new HashMap<>();
        if (string.isBlank()) {
            return screen;
        }
        String[] lines = string.split("\\r?\\n");
        int row = 0;
        boolean parsingForegroundColor = false;
        boolean parsingBackgroundColor = false;
        TextColor foregroundColor = ANSI.DEFAULT;
        TextColor backgroundColor = ANSI.DEFAULT;
        for (String line : lines) {
            for (int readColumn = 0, writeColumn = 0; readColumn < line.length();
                readColumn++, writeColumn++) {
                char character = line.charAt(readColumn);
                if (character == '§') {
                    writeColumn--;
                    if (!parsingForegroundColor) {
                        parsingForegroundColor = true;
                    } else if (!parsingBackgroundColor) {
                        parsingBackgroundColor = true;
                    }
                } else if (parsingBackgroundColor) {
                    writeColumn--;
                    backgroundColor = character == 'r' ? ANSI.BLACK
                        : reassignColorFromCode(backgroundColor, character);
                    parsingBackgroundColor = false;
                    parsingForegroundColor = false;
                } else if (parsingForegroundColor) {
                    writeColumn--;
                    foregroundColor = character == 'r' ? ANSI.WHITE_BRIGHT
                        : reassignColorFromCode(foregroundColor, character);
                    parsingForegroundColor = false;
                } else {
                    if (character != ' ') {
                        screen.put(Node.relativeTo(origin, row, writeColumn),
                            new TerminalCell(foregroundColor, backgroundColor, character));
                    } else if (!ignoreSpaces) {
                        screen.put(Node.relativeTo(origin, row, writeColumn), Cell.blank());
                    }
                }
            }
            row++;
        }
        return screen;
    }

    /**
     * Converts the provided board to a cell map, with the first board cell at (0, 0) relative to
     * the provided origin. If the board is null, an {@link IllegalArgumentException} is thrown. The
     * player argument is used to render the player’s cursor, so passing in a null player
     * effectively disables rendering board cursors.
     *
     * @param board  the board to convert to a cell map; only queried, not modified
     * @param player the player that owns the cursor to be rendered
     * @param origin the node location to place the first board cell
     * @return a new cell map representation of the board
     */
    static Map<Node, Cell> fromBoard(Board board, Player player, Node origin) {
        if (board == null) {
            throw new IllegalArgumentException("Cannot draw a null board to a new screen");
        }
        if (player == null) {
            player = new Player(UUID.randomUUID(), "Red", Team.RED);
        }
        if (origin == null) {
            origin = new Node(0, 0);
        }
        Map<Node, Cell> screen = new HashMap<>();
        for (int row = 0; row < board.height(); row++) {
            for (int column = 0; column < board.width(); column++) {
                Unit unit = board.getUnit(row, column);
                boolean hasCursor = board.board[row][column].containsCursor(player);
                if (unit != null) {
                    screen.put(Node.relativeTo(origin, row, column * 2),
                        hasCursor ? new TerminalCell(unit.foregroundColor(),
                            BoardCursor.fromPlayer(player).backgroundColor(), unit.character())
                            : unit.toCell());
                } else {
                    screen.put(Node.relativeTo(origin, row, column * 2),
                        hasCursor ? BoardCursor.fromPlayer(player) : Cell.blank());
                }
                if (column != board.width() - 1) {
                    screen.put(Node.relativeTo(origin, row, column * 2 + 1),
                        new TerminalCell(ANSI.DEFAULT, ANSI.BLACK, ' '));
                }
            }
        }
        return screen;
    }

    /**
     * Returns a copy of the provided string with color code characters removed. A color code
     * character is defined as “one or more ‘§’ characters plus the character immediately
     * following”.
     * <p>For instance, converting the string:
     * <pre>§fHello world!</pre>
     * <p>...would be converted to:
     * <pre>Hello world!</pre>
     * <p>...and accidentally adding a ‘§’ character somewhere and converting:
     * <pre>Hel§lo world!</pre>
     * <p>...would result in:
     * <pre>Helo world!</pre>
     *
     * @param string the string to remove color codes from
     * @return a new string with color codes removed
     */
    static String removeColorCodes(String string) {
        return string.replaceAll("§+.", "");
    }

    /**
     * Prunes this screen and any children from the display tree.
     */
    void close();

    /**
     * Color codes are special sequences of characters starting with one or more ‘§’ characters,
     * followed by an identifying character. When parsed by {@link #fromString}, they are extracted
     * and used to color the fore- and background colors of the rest of the string until another
     * color code is encountered or parsing is done.
     */
    enum ColorCode {
        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_CYAN('3'),
        DARK_RED('4'),
        DARK_MAGENTA('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        CYAN('b'),
        RED('c'),
        MAGENTA('d'),
        YELLOW('e'),
        WHITE('f'),
        RESET('r');

        private final char code;

        ColorCode(char code) {
            this.code = code;
        }

        /**
         * Returns a color code string representing this color as a background color. The resulting
         * string can be inserted or appended to a string to change its color when eventually
         * parsed in {@link #fromString}.
         *
         * @return the string representation of this as a background color
         */
        public String bgColor() {
            return "§§" + this.code;
        }

        /**
         * Returns a color code string representing this color as a foreground color. The resulting
         * string can be inserted or appended to a string to change its color when eventually
         * parsed in {@link #fromString}.
         *
         * @return the string representation of this as a foreground color
         */
        public String fgColor() {
            return "§" + this.code;
        }
    }

    private static TextColor reassignColorFromCode(TextColor color, char character) {
        switch (character) {
            case '0' -> color = ANSI.BLACK;
            case '1' -> color = ANSI.BLUE;
            case '2' -> color = ANSI.GREEN;
            case '3' -> color = ANSI.CYAN;
            case '4' -> color = ANSI.RED;
            case '5' -> color = ANSI.MAGENTA;
            case '6' -> color = ANSI.YELLOW;
            case '7' -> color = ANSI.WHITE;
            case '8' -> color = ANSI.BLACK_BRIGHT;
            case '9' -> color = ANSI.BLUE_BRIGHT;
            case 'a' -> color = ANSI.GREEN_BRIGHT;
            case 'b' -> color = ANSI.CYAN_BRIGHT;
            case 'c' -> color = ANSI.RED_BRIGHT;
            case 'd' -> color = ANSI.MAGENTA_BRIGHT;
            case 'e' -> color = ANSI.YELLOW_BRIGHT;
            case 'f' -> color = ANSI.WHITE_BRIGHT;
        }
        return color;
    }

    /**
     * Returns this screen’s height as a count of rows.
     * @return this screen’s row count
     */
    int rows();

    /**
     * Returns this screen’s width as a count of columns.
     * @return this screen’s column count
     */
    int columns();

}
