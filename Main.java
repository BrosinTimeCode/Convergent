import Controller.GameController;
import Model.TestBoard.BoardType;

public class Main {

    public static void main(String[] args) {
        int width;
        int height;
        if(args.length >= 2) {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } else {
            width = 0;
            height = 0;
        }
        GameController controller = new GameController(-1, BoardType.RANDOM, width, height);
        controller.initialize();
        while (true) {
            controller.getUserInput();
        }
    }
}
