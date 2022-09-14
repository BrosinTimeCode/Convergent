package Model;

import java.util.Random;
import Units.*;

public class TestBoard extends Board {

    // Random board generator for testing purposes
    public TestBoard(int rows, int columns) {
        super(rows, columns);
        Random randomGenerator = new Random(23);
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                int value = randomGenerator.nextInt(3);
                switch (value) {
                    case 0:
                        newUnit(row, column, BaseUnit.Team.RED, "Civilian");
                        break;
                    case 1:
                        newUnit(row, column, BaseUnit.Team.BLUE, "Tradesman");
                        break;
                    default:

                }
            }
        }
    }
}
