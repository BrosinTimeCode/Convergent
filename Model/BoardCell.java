package Model;

import Units.*;

public class BoardCell {

    public BaseUnit unit;

    public BoardCell(BaseUnit unit) {
        this.unit = unit;
    }

    public void emptyCell() {
        this.unit = null;
    }

    public boolean setUnit(BaseUnit unit) {
        if (this.unit == null) {
            this.unit = unit;
            return true;
        }
        return false;
    }
}
