package Model;

import java.util.Random;
import Units.*;

public class TestBoard extends Board {

    public int width;
    public int height;

    // Random board generator for testing purposes
    public TestBoard() {
        Random randomGenerator = new Random();
        width = randomGenerator.nextInt(20) + 1;
        height = randomGenerator.nextInt(20) + 1;
        board = new BoardCell[width][height];
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                int value = randomGenerator.nextInt(3);
                BaseUnit unit;
                switch (value) {
                    case 0:
                        unit = new Civilian();
                        break;
                    case 1:
                        unit = new Tradesman();
                        break;
                    default:
                        // No unit
                        unit = null;
                }
                board[row][column] = new BoardCell(unit);
            }
        }

    }
}
