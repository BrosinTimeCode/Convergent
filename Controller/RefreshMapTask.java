package Controller;

import Model.Board;
import View.GameViewInterface;
import java.util.TimerTask;

public class RefreshMapTask extends TimerTask {

    GameViewInterface viewInterface;
    Board board;

    public RefreshMapTask(GameViewInterface viewInterface, Board board) {
        this.viewInterface = viewInterface;
        this.board = board;
    }

    @Override
    public void run() {
        viewInterface.displayBoard(board);
    }
}
