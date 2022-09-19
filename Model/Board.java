package Model;

import Units.BaseUnit;
import java.util.HashMap;

/**
 * The Board class follows the model design in the MVC design pattern. This class receives
 * information from the controller to update within the board.
 */
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

    /**
     * Creates a string representation of the board. Empty spaces are represented by "0". Other
     * entities are represented by their name.
     *
     * @return A string representation of the board.
     */
    @Override
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

    /**
     * Finds a path from starting location to ending location.
     *
     * @param rowStart    An integer representing the row of starting location.
     * @param columnStart An integer representing the column of starting location.
     * @param rowEnd      An integer representing the row of ending location.
     * @param columnEnd   An integer representing the column of ending location.
     * @return A Path from starting location to ending location.
     */
    public Path pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd) {
        return pathFinder.pathFinder(rowStart, columnStart, rowEnd, columnEnd,
            board[rowStart][columnStart].unit.getTeam(), new HashMap<>());
    }

    /**
     * Moves BaseUnit unit to row and column in the board.
     *
     * @param column An integer representing destination column.
     * @param row    An integer representing destination row.
     * @param unit   The BaseUnit to be moved.
     */
    public void moveUnit(BaseUnit unit, int row, int column) {
        UnitLocation location = globalUnits.get(unit.getId());
        Path path = pathFinder(location.row, location.column, row, column);
        for (Node node : path.path) {
            if (board[node.row()][node.column()].unit != null) {
                // If an enemy node is blocking re-path
                if (!board[node.row()][node.column()].unit.isAlly(unit)) {
                    moveUnit(unit, row, column);
                    break;
                }
            }
            UnitLocation previousLocation = globalUnits.get(unit.getId());
            board[node.row()][node.column()].addUnit(unit);
            // If current unit populated last location remove it
            if (board[previousLocation.row][previousLocation.column].containsUnit(unit.getId())) {
                board[previousLocation.row][previousLocation.column].removeUnit(unit.getId());
            }
            globalUnits.get(unit.getId()).setLocation(node.row(), node.column());
        }
    }

    /**
     * Moves BaseUnit unit to the unit with the passed in id.
     *
     * @param id   An integer representing the id of the unit BaseUnit unit is moving to.
     * @param unit The BaseUnit to be moved.
     */
    public void moveToUnit(BaseUnit unit, int id) {
        UnitLocation location = globalUnits.get(id);
        moveUnit(unit, location.row, location.column);
    }

    /**
     * Creates a new unit at location. Calls UnitFactory to create unit with team and unitType.
     *
     * @param locationRow    An integer representing the row of the destination for the new unit.
     * @param locationColumn An integer representing the column of the destination for the new
     *                       unit.
     * @param team           A BaseUnit.Team that new unit will be on.
     * @param unitType       The type of unit to be created.
     */
    public void newUnit(int locationRow, int locationColumn, BaseUnit.Team team,
        String unitType) {
        BaseUnit unit = unitFactory.createUnit(unitType, team);
        board[locationRow][locationColumn].addUnit(unit);
        globalUnits.put(unit.getId(), new UnitLocation(locationRow, locationColumn, unit));
    }

    /**
     * Removes the unit with id from board and global units.
     *
     * @param id An integer representing the id of unit to be killed.
     */
    public void killUnit(int id) {
        UnitLocation deadUnitLocation = globalUnits.get(id);
        board[deadUnitLocation.row][deadUnitLocation.column].removeUnit(id);
        globalUnits.remove(id);
    }

    /**
     * Returns visible BaseUnit at row and column on the board.
     *
     * @param row    An integer representing the row on the board of the unit.
     * @param column An integer representing the column on the board of the unit.
     * @return BaseUnit at the row and column on the board.
     */
    public BaseUnit getUnit(int row, int column) {
        return board[row][column].unit;
    }

    /**
     * Returns BaseUnit identified by id.
     *
     * @param id An integer representing the id of the unit.
     * @return BaseUnit identified by id.
     */
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
