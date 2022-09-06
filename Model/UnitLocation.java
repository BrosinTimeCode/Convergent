package Model;

import Units.BaseUnit;

public class UnitLocation {

    protected int row;
    protected int column;
    private BaseUnit unit;

    public UnitLocation(int row, int column, BaseUnit unit) {
        this.row = row;
        this.column = column;
        this.unit = unit;
    }

}
