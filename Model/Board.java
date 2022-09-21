package Model;

import Units.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    public BoardCell[][] board;
    private PathFinder pathFinder;
    private UnitFactory unitFactory;
    private HashMap<Integer, UnitLocation> globalUnits;

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

    public void moveUnit(BaseUnit unit, int row, int column) {
        UnitLocation location = globalUnits.get(unit.getId());
        Path path = pathFinder(location.row, location.column, row, column);
        if(path == null) {
            return;
        }
        for (Node node : path.path) {
            if(board[node.getRow()][node.getColumn()].unit != null) {
                // If an enemy node is blocking re-path
                if (!board[node.getRow()][node.getColumn()].unit.isAlly(unit)) {
                    moveUnit(unit, row, column);
                    break;
                }
            }
            UnitLocation previousLocation = globalUnits.get(unit.getId());
            board[node.getRow()][node.getColumn()].addUnit(unit);
            // If current unit populated last location remove it
            if (board[previousLocation.row][previousLocation.column].containsUnit(unit.getId())) {
                board[previousLocation.row][previousLocation.column].removeUnit(unit.getId());
            }
            globalUnits.get(unit.getId()).setLocation(node.getRow(), node.getColumn());
        }
    }

    public void moveToUnit(BaseUnit unit, int id) {
        UnitLocation location = globalUnits.get(id);
        moveUnit(unit, location.row, location.column);
    }

    public void newUnit(int locationRow, int locationColumn, BaseUnit.Team team,
      String unitType) {
        BaseUnit unit = unitFactory.createUnit(unitType, team);
        board[locationRow][locationColumn].addUnit(unit);
        globalUnits.put(unit.getId(), new UnitLocation(locationRow, locationColumn, unit));
    }

    public void killUnit(int id) {
        UnitLocation deadUnitLocation = globalUnits.get(id);
        board[deadUnitLocation.row][deadUnitLocation.column].removeUnit(id);
        globalUnits.remove(id);
    }

    public BaseUnit getUnit(int x, int y) {
        return board[x][y].unit;
    }

    public BaseUnit getUnit(int id) {
        return globalUnits.get(id).getUnit();
    }

    public int getBoardHeight() {
        return board.length;
    }

    public int getBoardWidth() {
        return board[0].length;
    }

}
