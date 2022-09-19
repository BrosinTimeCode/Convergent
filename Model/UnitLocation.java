package Model;

import Units.BaseUnit;

/**
 * The UnitLocation class contains the location of a BaseUnit.
 */
public class UnitLocation {

    protected int row;
    protected int column;
    private final BaseUnit unit;

    public UnitLocation(int row, int column, BaseUnit unit) {
        this.row = row;
        this.column = column;
        this.unit = unit;
    }

    public void setLocation(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public BaseUnit getUnit() {
        return unit;
    }

}
