package Model;

import Units.*;
import java.util.HashMap;

public class BoardCell {

    public BaseUnit unit;
    HashMap<Integer, BaseUnit> units;

    public BoardCell(BaseUnit unit) {
        units = new HashMap<>();
        if(unit != null) {
            units.put(unit.getId(), unit);
        }
    }

    public void emptyCell() {
        this.unit = null;
    }

    public void removeUnit(int id) {
        units.remove(id);
    }

    public boolean setUnit(BaseUnit unit) {
        if (this.unit == null) {
            this.unit = unit;
            return true;
        }
        return false;
    }

    public void addUnit(BaseUnit unit) {
        units.put(unit.getId(), unit);
    }
}
