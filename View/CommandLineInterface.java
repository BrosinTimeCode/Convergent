package View;

import Model.Board;
import View.UserLogItem.Type;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandLineInterface implements GameViewInterface {

    Terminal terminal;
    TextGraphics textGraphics;
    private final int boardPositionX;
    private final int boardPositionY;
    private final int inputPositionX;
    private final int inputPositionY;
    private final int logPositionX;
    private final int logPositionY;
    List<UserLogItem> userLog;

    public CommandLineInterface(Board board) {

        terminal = null;
        boardPositionX = 2;
        boardPositionY = 1;
        inputPositionX = 2;
        inputPositionY = board.getBoardHeight() + 13;
        logPositionX = 2;
        logPositionY = board.getBoardHeight() + 2;
        userLog = new ArrayList<>();

    }

    public void displayBoard(Board board) {
        try {
            textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
            String[] rows = board.toString().split("\\r?\\n");
            for (int row = 0; row < rows.length; row++) {
                textGraphics.putString(boardPositionX, row + boardPositionY, rows[row]);
            }
            terminal.flush();
            resetTextGraphicsColors();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayHelp() {
        UserLogItem log = new UserLogItem(TextColor.ANSI.YELLOW,
          "Type \"m\" to move a unit or \"h\" for a list of commands.", Type.INFO);
        userLog.add(0, log);
        displayConsoleLog();
        System.out.println(log);
    }

    public void displayInvalidCommand() {
        UserLogItem log = new UserLogItem(TextColor.ANSI.RED,
          "Invalid command! Type \"h\" for a list of commands.", Type.INFO);
        userLog.add(log);
        displayConsoleLog();
        System.out.println(log);
    }

    public void displayCommandError(String error) {
        System.out.println(error);
    }

    @Override
    public void initialize() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            defaultTerminalFactory.setTerminalEmulatorTitle("RTS Game");
            TerminalSize terminalSize = new TerminalSize(inputPositionX+62, inputPositionY+2);
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

    @Override
    public String getUserInput() {
        int x = inputPositionX;
        int y = inputPositionY;
        try {
            resetTextGraphicsColors();
            textGraphics.putString(x, y, "/");
            terminal.flush();

            KeyStroke keyStroke = terminal.readInput();
            List<Character> input = new ArrayList<>();
            do {
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    break;
                }
                if (keyStroke.getKeyType().equals(KeyType.Character)) {
                    input.add(keyStroke.getCharacter());
                } else if (keyStroke.getKeyType().equals(KeyType.Backspace)
                  && input.size() >= 1) {
                    input.remove(input.size() - 1);
                }
                textGraphics.drawLine(x + 1, y,
                  terminal.getTerminalSize().getColumns() - 1, y,
                  ' ');
                if (input.size() < 1) {
                    textGraphics.putString(x + 1, y,
                      "");
                } else {
                    textGraphics.putString(x + 1, y,
                      input.toString().substring(1, 3 * input.size() - 1)
                        .replaceAll(", ", ""));
                }
                terminal.flush();
                keyStroke = terminal.readInput();
            } while (keyStroke.getKeyType() != KeyType.Enter);
            textGraphics.drawLine(x + 1, y,
              terminal.getTerminalSize().getColumns() - 1, y,
              ' ');
            terminal.flush();
            return input.toString().substring(1, 3 * input.size() - 1)
              .replaceAll(", ", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void refreshBoard(Board board) {
        displayBoard(board);
    }

    private void resetTextGraphicsColors() {
        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
    }

    public void displayConsoleLog() {
        int x = logPositionX;
        int y = logPositionY;
        try {
            // display the last five logs
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            for (int i = 0; i < 10; i++) {
                textGraphics.drawLine(x, y + i,
                  terminal.getTerminalSize().getColumns() - 1, y + i,
                  ' ');
            }
            for (int i = 0; i < 10 && i < userLog.size(); i++) {
                textGraphics.setForegroundColor(userLog.get(userLog.size()-i-1).getColor());
                textGraphics.putString(x, y + 9 - i, userLog.get(userLog.size()-i-1).getMemo());
            }
            resetTextGraphicsColors();
            terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
