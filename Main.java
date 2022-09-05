import Controller.GameController;

public class Main {

    public static void main(String[] args) {
        int[] boardSize = {10};
        GameController controller = new GameController(-1, boardSize);
        controller.initialize();
        controller.handleUserInput();
    }
}
