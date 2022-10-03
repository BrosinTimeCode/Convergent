package com.brosintime.rts.Model;

import java.util.Random;

/**
 * The TestBoard class is a type of board that has different preset boards based on the passed in
 * BoardType enum.
 */
public class TestBoard extends Board {

    // Random board generator for testing purposes
    public TestBoard(BoardType boardType, int rows, int columns) {
        super(rows, columns);
        switch (boardType) {
            case RANDOM:
                randomBoard();
                break;
            case MONOVSMONO:
                newUnit(0, 0, Player.Team.RED, "Civilian");
                newUnit(rows - 1, columns - 1, Player.Team.BLUE, "Civilian");
                break;
            case LONESURVIVOR:
                newUnit(0, 0, Player.Team.RED, "Civilian");
                break;
            case ONEVSEVERYONE:
                oneVSEveryone();
                break;
            case ONEVSCHECKERBOARD:
                oneVSCheckerboard();
                break;
        }
    }

    /**
     * Creates a board that has random BLUE and RED units on each BoardCell.
     */
    private void randomBoard() {
        Random randomGenerator = new Random();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                int value = randomGenerator.nextInt(5);
                switch (value) {
                    case 0:
                        newUnit(row, column, Player.Team.RED, "Civilian");
                        break;
                    case 1:
                        newUnit(row, column, Player.Team.RED, "Tradesman");
                        break;
                    case 3:
                        newUnit(row, column, Player.Team.BLUE, "Civilian");
                        break;
                    case 4:
                        newUnit(row, column, Player.Team.BLUE, "Tradesman");
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Creates a board completely covered with RED units with one BLUE unit in the corner.
     */
    private void oneVSEveryone() {
        Random randomGenerator = new Random();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (row == 0 && column == 0) {
                    newUnit(row, column, Player.Team.BLUE, "Civilian");
                }
                int value = randomGenerator.nextInt(2);
                switch (value) {
                    case 0:
                        newUnit(row, column, Player.Team.RED, "Civilian");
                        break;
                    case 1:
                        newUnit(row, column, Player.Team.RED, "Tradesman");
                        break;
                }
            }
        }
    }

    /**
     * Creates a board with random RED units in a checkerboard pattern with a BLUE unit in the
     * corner.
     */
    private void oneVSCheckerboard() {
        Random randomGenerator = new Random();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (row == 0 && column == 0) {
                    newUnit(row, column, Player.Team.BLUE, "Civilian");
                }
                if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                    int value = randomGenerator.nextInt(2);
                    switch (value) {
                        case 0:
                            newUnit(row, column, Player.Team.RED, "Civilian");
                            break;
                        case 1:
                            newUnit(row, column, Player.Team.RED, "Tradesman");
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
