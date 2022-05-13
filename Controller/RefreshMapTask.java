package Controller;
import View.GameViewInterface;
import Model.BaseBoard;
import java.util.TimerTask;

public class RefreshMapTask extends TimerTask {
    GameViewInterface viewInterface;
    BaseBoard board;
    public RefreshMapTask(GameViewInterface viewInterface, BaseBoard board) {
        this.viewInterface = viewInterface;
        this.board = board;
    }
    @Override
    public void run() {
        viewInterface.displayBoard(board);
    }
}
