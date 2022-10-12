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

public interface Drawable {

    Map<Node, Cell> toCells();

    Drawable parent();

    List<Drawable> children();

    void setParent(Drawable parent);

    boolean visible();

    void show();

    void hide();

    Drawable asHidden();

    Drawable asChild(Drawable parent);

    Drawable firstAncestor();

    Node origin();

    void onRender();

    void onHidden();

    boolean justHidden();

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

    static Node originCenteredHorizontallyOn(Map<Node, Cell> cells, Drawable screen) {
        Node offset = offsetFrom(cells, screen);
        return Node.relativeTo(screen.origin(), 0, offset.column());
    }

    static Node originCenteredVerticallyOn(Map<Node, Cell> cells, Drawable screen) {
        Node offset = offsetFrom(cells, screen);
        return Node.relativeTo(screen.origin(), offset.row(), 0);
    }

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

    static String removeColorCodes(String string) {
        return string.replaceAll("§+.", "");
    }

    void close();

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

        public String bgColor() {
            return "§§" + this.code;
        }

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

    int rows();

    int columns();

}
