import Controller.GameController;
public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController(-1, 1);
        controller.initialize();
        controller.handleUserInput();
    }
}
