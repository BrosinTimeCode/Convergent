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
}
