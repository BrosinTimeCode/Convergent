package Model;

import Units.BaseUnit;

import java.util.HashMap;

public class Board {

    public BoardCell[][] board;
    private PathFinder pathFinder;
    private UnitFactory unitFactory;
    private HashMap<Integer, BaseUnit> globalUnits;

    public Board(int rows, int columns) {
        board = new BoardCell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
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
        return pathFinder.pathFinder(rowStart, columnStart, rowEnd, columnEnd,
          board[rowStart][columnStart].unit.getTeam(), new HashMap<>());
    }

    public boolean newUnit(int locationRow, int locationColumn, BaseUnit.Team team,
      String unitType) {
        if (board[locationRow][locationColumn].unit == null) {
            BaseUnit unit = unitFactory.createUnit(unitType, team);
            globalUnits.put(unit.getId(), unit);
            board[locationRow][locationColumn] = new BoardCell(unit);
            return true;
        }
        return false;
    }

    public BaseUnit getUnit(int x, int y) {
        return board[x][y].unit;
    }

    public int getBoardHeight() {
        return board.length;
    }

    public int getBoardWidth() {
        return board[0].length;
    }

}
