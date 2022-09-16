package View;

import Model.Board;
import com.googlecode.lanterna.input.KeyStroke;

public interface GameViewInterface {

    // Method to display the current state of the board
    void displayBoard(Board board);

    // Displays help prompt
    void displayHelp();

    void displayInvalidCommand();

    void initialize();

    KeyStroke getUserKeyStroke();

    void displayConsoleLog();

    void displayInput(String input);

    void clearInput();
}
