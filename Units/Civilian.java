package Units;

import View.CommandLineCell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

public class Civilian extends FootUnit {

    public Civilian(Team team, int id) {
        name = 'C';
        speed = 2;
        health = 100;
        rangeSight = 4;
        rangeAttack = 2;
        attack = 5;
        armor = 5;
        rangeAct = 1;
        super.team = team;
        super.id = id;
    }

    @Override
    public CommandLineCell toCommandLineCell() {
        TextColor foregroundColor = ANSI.DEFAULT;
        switch (team) {
            case RED -> foregroundColor = ANSI.RED_BRIGHT;
            case BLUE -> foregroundColor = ANSI.BLUE_BRIGHT;
        }
        return new CommandLineCell(foregroundColor, ANSI.DEFAULT, name);
    }
}
