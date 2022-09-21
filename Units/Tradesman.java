package Units;

import View.CommandLineCell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

public class Tradesman extends FootUnit {

    public Tradesman(Team team, int id) {
        name = 'T';
        speed = 1;
        health = 100;
        rangeSight = 5;
        rangeAttack = 1;
        attack = 3;
        armor = 3;
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
