package com.brosintime.rts.Model;

import com.brosintime.rts.Model.Units.Unit;

/**
 * The UnitLocation class contains the location of a BaseUnit.
 */
public class UnitLocation {

    protected int row;
    protected int column;
    private final Unit unit;

    public UnitLocation(int row, int column, Unit unit) {
        this.row = row;
        this.column = column;
        this.unit = unit;
    }

    public void setLocation(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Unit getUnit() {
        return unit;
    }

}
