package Model;

import java.util.Random;
import Units.*;

public class TestBoard extends Board {

    // Random board generator for testing purposes
    public TestBoard() {
        Random randomGenerator = new Random();
        int x = randomGenerator.nextInt(20) + 1;
        int y = randomGenerator.nextInt(20) + 1;
        board = new BoardCell[x][y];
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
}
