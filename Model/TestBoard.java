package Model;

import java.util.Random;
import Units.*;

public class TestBoard extends Board {

    // Random board generator for testing purposes
    public TestBoard(BoardType boardType, int rows, int columns) {
        super(rows, columns);
        switch(boardType) {
            case RANDOM:
                randomBoard();
                break;
            case MONOVSMONO:
                newUnit(0, 0, BaseUnit.Team.RED, "Civilian");
                newUnit(rows - 1, columns - 1, BaseUnit.Team.BLUE, "Civilian");
                break;
            case LONESURVIVOR:
                newUnit(0, 0, BaseUnit.Team.RED, "Civilian");
                break;
            case ONEVSEVERYONE:
                oneVSEveryone();
                break;
            case ONEVSCHECKERBOARD:
                oneVSCheckerboard();
                break;
        }
    }

    private void randomBoard() {
        Random randomGenerator = new Random();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                int value = randomGenerator.nextInt(5);
                switch (value) {
                    case 0:
                        newUnit(row, column, BaseUnit.Team.RED, "Civilian");
                        break;
                    case 1:
                        newUnit(row, column, BaseUnit.Team.RED, "Tradesman");
                        break;
                    case 3:
                        newUnit(row, column, BaseUnit.Team.BLUE, "Civilian");
                        break;
                    case 4:
                        newUnit(row, column, BaseUnit.Team.BLUE, "Tradesman");
                        break;
                    default:
                }
            }
        }
    }

    private void oneVSEveryone() {
        Random randomGenerator = new Random();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if(row == 0 && column == 0) {
                    newUnit(row, column, BaseUnit.Team.BLUE, "Civilian");
                }
                int value = randomGenerator.nextInt(2);
                switch (value) {
                    case 0:
                        newUnit(row, column, BaseUnit.Team.RED, "Civilian");
                        break;
                    case 1:
                        newUnit(row, column, BaseUnit.Team.RED, "Tradesman");
                        break;
                }
            }
        }
    }

    private void oneVSCheckerboard() {
        Random randomGenerator = new Random();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if(row == 0 && column == 0) {
                    newUnit(row, column, BaseUnit.Team.BLUE, "Civilian");
                }
                if((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                    int value = randomGenerator.nextInt(2);
                    switch (value) {
                        case 0:
                            newUnit(row, column, BaseUnit.Team.RED, "Civilian");
                            break;
                        case 1:
                            newUnit(row, column, BaseUnit.Team.RED, "Tradesman");
                            break;
                    }
                }
            }
        }
    }

    public enum BoardType {
        EMPTY,
        RANDOM,
        MONOVSMONO,
        LONESURVIVOR,
        ONEVSEVERYONE,
        ONEVSCHECKERBOARD,
    }
}
