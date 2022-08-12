package View;

import Model.BaseBoard;
import Model.Board;

public class CommandLineInterface implements GameViewInterface {

  public CommandLineInterface() {

  }

  public void displayBoard(BaseBoard board) {
    System.out.println(board);
  }

  public void refreshBoard(Board board) {
    displayBoard(board);
  }
}
