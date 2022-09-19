package Model;

import Units.BaseUnit;
import Units.Civilian;
import Units.Tradesman;

/**
 * The UnitFactory class follows the factory design pattern and creates units with unique ids.
 */
public class UnitFactory {

    private int identifier;

    public UnitFactory() {
        identifier = 0;
    }

    public BaseUnit createUnit(String unitType, BaseUnit.Team team) {
        if (unitType.equals("Civilian")) {
            return new Civilian(team, identifier++);
        } else if (unitType.equals("Tradesman")) {
            return new Tradesman(team, identifier++);
        }
        return null;
    }
}
