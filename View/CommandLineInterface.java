package View;

import Model.BaseBoard;
import Model.Board;

public class CommandLineInterface implements GameViewInterface {
    public CommandLineInterface(){

    }
    public void displayBoard(BaseBoard board) {
        System.out.println(board);
    }
    public void displayHelp() { System.out.println("Type \"m\" to move a unit or \"h\" for a list of commands."); }
    public void displayInvalidCommand() { System.out.println("Invalid command! Type \"h\" for a list of commands."); }
    public void refreshBoard(Board board) {
    displayBoard(board);}
}
