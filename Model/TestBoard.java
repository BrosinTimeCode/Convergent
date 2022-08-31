package Model;

import java.util.Random;
import Units.*;

public class TestBoard extends Board {

    private final int width;
    private final int height;
    // Random board generator for testing purposes
    public TestBoard() {
        Random randomGenerator = new Random();
        width = randomGenerator.nextInt(20) + 1;
        height = randomGenerator.nextInt(20) + 1;
        board = new BoardCell[height][width];
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                int value = randomGenerator.nextInt(3);
                BaseUnit unit;
                switch (value) {
                    case 0:
                        unit = new Civilian(BaseUnit.Team.BLUE, 1);
                        break;
                    case 1:
                        unit = new Tradesman(BaseUnit.Team.BLUE, 1);
                        break;
                    default:
                        // No unit
                        unit = null;
                }
                board[row][column] = new BoardCell(unit);
            }
        }
    }
    public int getHeight() { return height; }
    public int getWidth() { return width; }
}
