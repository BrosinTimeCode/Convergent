package View;

import Model.Board;
import Model.Node;
import Units.BaseUnit;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.HashMap;

public class CommandLineRenderer {

    private int width;
    private int height;
    private int refreshRate;
    private final HashMap<Node, CommandLineCell> grid = new HashMap<>();
    private final HashMap<Node, CommandLineCell> screen = new HashMap<>();
    private Terminal terminal;
    private TextGraphics textGraphics;

    public CommandLineRenderer(int width, int height, int refreshRate) {
        this.width = Math.max(width, 1);
        this.height = Math.max(height, 1);
        this.refreshRate = Math.max(refreshRate, 1);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.grid.put(new Node(y, x), CommandLineCell.newBlank());
            }
        }
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            defaultTerminalFactory.setTerminalEmulatorTitle("RTS Game");
            TerminalSize terminalSize = new TerminalSize(this.width, this.height);
            defaultTerminalFactory.setInitialTerminalSize(terminalSize);
            terminal = defaultTerminalFactory.createTerminal();
            textGraphics = terminal.newTextGraphics();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);
            terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void optimizeScreen() {
        for (Node node : this.screen.keySet()) {
            if (this.screen.get(node).equals(this.grid.get(node))) {
                this.screen.remove(node);
            }
        }
    }

    public void flush() throws IOException {
        for (Node node : this.screen.keySet()) {
            CommandLineCell cell = this.screen.get(node);
            terminal.setCursorPosition(node.getColumn(), node.getRow());
            terminal.setBackgroundColor(cell.getBackgroundColor());
            terminal.setForegroundColor(cell.getForegroundColor());
            terminal.putCharacter(cell.getCharacter());
            this.grid.put(node, cell);
        }
        this.terminal.flush();
    }

    public void refresh() throws IOException {
        terminal.clearScreen();
        for (Node node : this.grid.keySet()) {
            CommandLineCell cell = this.grid.get(node);
            if (!cell.isBlank()) {
                terminal.setCursorPosition(node.getColumn(), node.getRow());
                terminal.setBackgroundColor(cell.getBackgroundColor());
                terminal.setForegroundColor(cell.getForegroundColor());
                terminal.putCharacter(cell.getCharacter());
            }
        }
        this.terminal.flush();
    }

    public void putCell(CommandLineCell cell, int x, int y) {
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        this.screen.put(new Node(y, x), cell);
    }

    public CommandLineCell getCell(int x, int y) {
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        return this.screen.get(new Node(y, x));
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRefreshRate() {
        return this.refreshRate;
    }

    public void setWidth(int width) {
        this.width = Math.max(width, 1);
    }

    public void setHeight(int height) {
        this.height = Math.max(height, 1);
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = Math.max(refreshRate, 1);
    }

    public void putBoard(Board board, int x, int y) {
        if (board == null) {
            return;
        }
        x = constrictToBounds(x, this.width - 1);
        y = constrictToBounds(y, this.height - 1);
        for (int column = 0; column < board.getBoardWidth(); column++) {
            for (int row = 0; row < board.getBoardHeight(); row++) {
                BaseUnit unit = board.getUnit(column, row);
                if (unit != null) {
                    this.screen.put(new Node(row + y, column + x), unit.toCommandLineCell());
                }
            }
        }
    }

    private int constrictToBounds(int coordinate, int upperBound) {
        coordinate = Math.max(coordinate, 0);
        coordinate = Math.min(coordinate, upperBound);
        return coordinate;
    }
}
