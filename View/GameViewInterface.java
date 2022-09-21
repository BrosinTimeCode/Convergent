package View;

import Model.Board;
import com.googlecode.lanterna.input.KeyStroke;

/**
 * The GameViewInterface interface follows the view design in the MVC design pattern. This class
 * handles userinput and sends information and commands to the controller.
 */
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

    void setConsoleLogHeight(int height);

    int getConsoleLogHeight();
}
