package com.brosintime.rts.Units;

import com.brosintime.rts.Model.Player.Team;
import com.brosintime.rts.View.Cell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

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

    Team team();

    boolean isAlly(Unit unit);

    int id();

    boolean damageEntity(Unit unit);

    int attack();

}
