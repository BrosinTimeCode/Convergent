package com.brosintime.rts.Units;

import com.brosintime.rts.View.Cell;
import com.brosintime.rts.View.CommandLineCell;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

public abstract class BaseUnit implements Unit {

    protected int id;
    protected Team team;
    protected char name = 'X';
    protected int health = 100;
    protected int attack = 1;
    protected int armor = 5;
    protected boolean isSelected = false;

    @Override
    public Team team() {
        return team;
    }

    @Override
    public boolean isAlly(Unit unit) {
        return unit.team() == this.team;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public boolean damageEntity(Unit unit) {
        // TODO: Add math for damaging units according to our theme
        if (unit.attack() - armor > 0) {
            health = health - (unit.attack() - armor);
        }
        return health <= 0;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public Cell toCell() {
        return new CommandLineCell(foregroundColor(), backgroundColor(), character());
    }

    @Override
    public TextColor foregroundColor() {
        return Unit.getColorByTeam(this.team);
    }

    @Override
    public TextColor backgroundColor() {
        if (this.isSelected) {
            return ANSI.GREEN;
        }
        return ANSI.DEFAULT;
    }

    @Override
    public char character() {
        return this.name;
    }

    @Override
    public void select() {
        this.isSelected = true;
    }

    @Override
    public void deselect() {
        this.isSelected = false;
    }

    @Override
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override
    public int attack() {
        return this.attack;
    }

}