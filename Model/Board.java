package Model;

import Units.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Board extends BaseBoard {

    public BoardCell[][] board;
    private PathFinder pathFinder;
    private UnitFactory unitFactory;
    private HashMap<Integer, BaseUnit> globalUnits;
    private BaseUnit player1SelectedUnit;


    public Board(int row, int column) {
        board = new BoardCell[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = new BoardCell(null);
            }
        }
        pathFinder = new PathFinder(this.board);
        unitFactory = new UnitFactory();
        globalUnits = new HashMap<>();
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

    public boolean newUnit(int locationRow, int locationColumn, BaseUnit.Team team, String unitType) {
        if(board[locationRow][locationColumn].unit == null) {
            BaseUnit unit = unitFactory.createUnit(unitType, team);
            globalUnits.put(unit.getId(), unit);
            board[locationRow][locationColumn] = new BoardCell(unit);
            return true;
        }
        return false;
    }

    public boolean selectUnit(int row, int column) {
        if(checkBounds(row, column)) {
            player1SelectedUnit = board[row][column].unit;
            return true;
        }
        return false;
    }

    private boolean checkBounds(int row, int column) {
        return !(row > board.length || row < 0 || column > board[0].length || column < 0);
    }

}
