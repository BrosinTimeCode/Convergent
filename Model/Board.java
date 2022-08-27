package Model;

import Units.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Board extends BaseBoard {

    public BoardCell[][] board;
    private PathFinder pathFinder;

    public Board(int row, int column) {
        board = new BoardCell[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = new BoardCell(null);
            }
        }
        pathFinder = new PathFinder(this.board);
    }

    public Board() {
    }

    @Override
    // toString method used for printing the board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (BoardCell[] row : board) {
            for (BoardCell column : row) {
                // If there is no unit in cell
                if (column.unit == null) {
                    builder.append("0");
                } else {
                    builder.append(column.unit.getName());
                }
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public Path pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd) {
        return pathFinder.pathFinder(rowStart, columnStart, rowEnd, columnEnd, board[rowStart][columnStart].unit.getTeam(), new HashMap<>());
    }

}
