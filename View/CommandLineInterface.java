package View;

import Model.BaseBoard;
import Model.Board;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;

public class CommandLineInterface implements GameViewInterface {

    Terminal terminal = null;

    public CommandLineInterface() {

    }

    public void displayBoard(BaseBoard board) {
        final TextGraphics textGraphics;
        try {
            textGraphics = terminal.newTextGraphics();
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            String[] rows = board.toString().split("\\r?\\n");
            for (int row = 0; row < rows.length; row++) {
                textGraphics.putString(1, row, rows[row]);
            }
            terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayHelp() {
        System.out.println("Type \"m\" to move a unit or \"h\" for a list of commands.");
    }

    public void displayInvalidCommand() {
        System.out.println("Invalid command! Type \"h\" for a list of commands.");
    }

    public void displayCommandError(String error) {
        System.out.println(error);
    }

    @Override
    public void initialize() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBoard(Board board) {
        displayBoard(board);
    }
}
