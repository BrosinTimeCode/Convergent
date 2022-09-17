package Model;

import Units.*;
import java.util.HashMap;

public class BoardCell {

    public BaseUnit unit;
    private final HashMap<Integer, BaseUnit> units;

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
        // Current unit is visible unit
        if(units.get(id) == unit) {
            units.remove(id);
            if(units.isEmpty()) {
                unit = null;
            } else {
                for(Integer integer: units.keySet()) {
                    unit = units.get(integer);
                    break;
                }
            }
        }
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
        this.unit = unit;
    }

    public boolean containsUnit(int id) {
        return units.containsKey(id);
    }
}
