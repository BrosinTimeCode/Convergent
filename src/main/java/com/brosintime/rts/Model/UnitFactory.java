package com.brosintime.rts.Model;

import com.brosintime.rts.Model.Player.Team;
import com.brosintime.rts.Units.BaseUnit;
import com.brosintime.rts.Units.Civilian;
import com.brosintime.rts.Units.Tradesman;

/**
 * The UnitFactory class follows the factory design pattern and creates units with unique ids.
 */
public class UnitFactory {

    private int identifier;

    public UnitFactory() {
        identifier = 0;
    }

    public BaseUnit createUnit(String unitType, Team team) {
        if (unitType.equals("Civilian")) {
            return new Civilian(team, identifier++);
        } else if (unitType.equals("Tradesman")) {
            return new Tradesman(team, identifier++);
        }
        return null;
    }
}
