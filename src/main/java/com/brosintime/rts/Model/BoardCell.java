package com.brosintime.rts.Model;

import com.brosintime.rts.Units.Unit;
import java.util.HashMap;

/**
 * The BoardCell class contains a visible unit along with multiple invisible units.
 */
public class BoardCell {

    public Unit unit;
    private final HashMap<Integer, Unit> units;

    public BoardCell(Unit unit) {
        units = new HashMap<>();
        if (unit != null) {
            units.put(unit.id(), unit);
            this.unit = unit;
        }
    }

    /**
     * Removes unit in BoardCell with specified id. If BoardCell is not empty visible unit will be
     * selected from other units in BoardCell.
     *
     * @param id An integer representing the unit to be removed.
     */
    public void removeUnit(int id) {
        // Current unit is visible unit
        if (units.get(id) == unit) {
            units.remove(id);
            if (units.isEmpty()) {
                unit = null;
            } else {
                for (Integer integer : units.keySet()) {
                    unit = units.get(integer);
                    break;
                }
            }
        }
    }

    /**
     * Adds a unit to BoardCell. Added unit becomes visible unit in BoardCell.
     *
     * @param unit BaseUnit to be added to BoardCell.
     */
    public void addUnit(Unit unit) {
        units.put(unit.id(), unit);
        this.unit = unit;
    }

    /**
     * Checks if BoardCell has unit identified by id.
     *
     * @param id An integer representing the id of the unit to search for.
     * @return A boolean showing if the BoardCell contains unit.
     */
    public boolean containsUnit(int id) {
        return units.containsKey(id);
    }
}
