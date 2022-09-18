package Controller;

import Model.Board;
import View.GameViewInterface;
import java.util.TimerTask;

/**
 * The RefreshMapTask class is used to periodically damage entities that are under attack by other
 * entities. This class extends TimerTask and the time set for running this task is the rate at
 * which the map is refreshed.
 */
public class RefreshMapTask extends TimerTask {

    GameViewInterface viewInterface;
    Board board;

    public RefreshMapTask(GameViewInterface viewInterface, Board board) {
        this.viewInterface = viewInterface;
        this.board = board;
    }

    /**
     * Periodically displays board.
     */
    @Override
    public void run() {
        viewInterface.displayBoard(board);
    }
}