package View;

import Controller.GameController;
import Log.UserLog;
import Log.UserLogItem;
import Model.Board;
import Log.UserLogItem.Type;
import Units.BaseUnit;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
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

    public CommandLineInterface(Board board) {

        terminal = null;
        boardPositionX = 5;
        boardPositionY = 4;
        inputPositionX = 2;
        inputPositionY = board.getBoardHeight() + 17;
        logPositionX = 2;
        logPositionY = board.getBoardHeight() + 6;

    }

    public void displayBoard(Board board) {
        try {
            // board
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            String[] rows = board.toString().split("\\r?\\n");
            for (int row = 0; row < rows.length; row++) {
                String[] cols = rows[row].split(" ");
                for (int col = 0; col < cols.length; col++) {
                    BaseUnit unit = board.getUnit(row, col);
                    textGraphics.setForegroundColor(GameController.isPlayer1SelectedUnit(unit) && unit != null ? TextColor.ANSI.CYAN_BRIGHT : TextColor.ANSI.WHITE);
                    textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
                    textGraphics.putString(col * 2 + boardPositionX, row + boardPositionY, cols[col] + " ");
                }
            }

            // border
            textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
            textGraphics.putString(boardPositionX - 1, boardPositionY - 1,
              " ".repeat(board.getBoardWidth() * 2 + 2));
            for (int i = 0; i < board.getBoardHeight(); i++) {
                textGraphics.putString(boardPositionX - 1, boardPositionY + i, " ");
                textGraphics.putString(boardPositionX + board.getBoardWidth() * 2,
                  boardPositionY + i, " ");
            }
            textGraphics.putString(boardPositionX - 1, boardPositionY + board.getBoardHeight(),
              " ".repeat(board.getBoardWidth() * 2 + 2));

            // coordinates - Y
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            for (int i = 0; i < board.getBoardHeight(); i++) {
                textGraphics.putString(i >= 10 ? boardPositionX - 3 : boardPositionX - 2,
                  boardPositionY + i, String.valueOf(i));
            }

            // coordinates - X
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            for (int i = 0; i < board.getBoardWidth(); i++) {
                textGraphics.putString(boardPositionX + i * 2, boardPositionY - 2, String.valueOf(i % 10));
            }
            if (board.getBoardWidth() >= 10) {
                for (int i = 10; i < board.getBoardWidth(); i++) {
                    textGraphics.putString(boardPositionX + i * 2, boardPositionY - 3, String.valueOf(i / 10));
                }
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
        UserLog.add(log);
        displayConsoleLog();
    }

    public void displayInvalidCommand() {
        UserLogItem log = new UserLogItem(TextColor.ANSI.RED,
          "Invalid command! Type \"h\" for a list of commands.", Type.INFO);
        UserLog.add(log);
        displayConsoleLog();
    }

    public void displayCommandError(String error) {
        System.out.println(error);
    }

    @Override
    public void initialize() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            defaultTerminalFactory.setTerminalEmulatorTitle("RTS Game");
            TerminalSize terminalSize = new TerminalSize(inputPositionX + 82, inputPositionY + 2);
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

    @Override
    public void displayConsoleLog() {
        // TODO: add check for UserLogItem scope (DEV, CHAT, INFO)
        int x = logPositionX;
        int y = logPositionY;
        try {
            // display the last five logs
            textGraphics.setForegroundColor(ANSI.WHITE);
            textGraphics.setBackgroundColor(ANSI.BLACK);
            for (int i = 0; i < 10; i++) {
                textGraphics.drawLine(x, y + i,
                  terminal.getTerminalSize().getColumns() - 1, y + i,
                  ' ');
            }
            for (int i = 0; i < 10 && i < UserLog.LOGS.size(); i++) {
                textGraphics.setForegroundColor(
                  UserLog.LOGS.get(UserLog.LOGS.size() - i - 1).getColor());
                textGraphics.putString(x, y + 9 - i,
                  UserLog.LOGS.get(UserLog.LOGS.size() - i - 1).getMemo());
            }
            resetTextGraphicsColors();
            terminal.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
