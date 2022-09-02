package View;

import Log.UserLogItem;
import Model.Board;

public interface GameViewInterface {

    // Method to display the current state of the board
    void displayBoard(Board board);

    // Method to refresh the state of the board
    void refreshBoard(Board board);

    // Displays help prompt
    void displayHelp();

    void displayInvalidCommand();

    void displayCommandError(String error);

    void initialize();

    String getUserInput();

}
