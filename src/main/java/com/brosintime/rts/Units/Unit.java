package com.brosintime.rts.Units;

import com.brosintime.rts.Model.Player.Team;
import com.brosintime.rts.View.Cell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

/**
 * A unit capable of team-assignment.
 */
public interface Unit extends Cell, Selectable {

    static TextColor getColorByTeam(Team team) {
        if (team == null) {
            return ANSI.DEFAULT;
        }
        switch (team) {
            case RED -> {
                return ANSI.RED_BRIGHT;
            }
            case BLUE -> {
                return ANSI.BLUE_BRIGHT;
            }
        }
        return ANSI.DEFAULT;
    }

    /**
     * Retrieves this unit’s team color.
     *
     * @return the team
     */
    Team team();

    /**
     * Determines if the provided unit is an ally of this unit.
     *
     * @param unit the unit to compare
     * @return {@code true} if the unit is an ally, {@code false} if not
     */
    boolean isAlly(Unit unit);

    /**
     * Retrieves this unit’s ID.
     *
     * @return the unit’s ID
     */
    int id();

    boolean damageEntity(Unit unit);

    /**
     * Retrieves this unit’s current attack power.
     *
     * @return the attack power
     */
    int attack();

}
