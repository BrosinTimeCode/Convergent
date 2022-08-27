package View;

import Model.BaseBoard;
import Model.Board;

public interface GameViewInterface {

    // Method to display the current state of the board
    void displayBoard(BaseBoard board);

    // Method to refresh the state of the board
    void refreshBoard(Board board);

    // Displays help prompt
    void displayHelp();

    void displayInvalidCommand();

    void displayCommandError(String error);

    void initialize();
}
