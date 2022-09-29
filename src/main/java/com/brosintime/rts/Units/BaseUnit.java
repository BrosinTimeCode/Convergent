package com.brosintime.rts.Units;

public abstract class BaseUnit {

    char name = 'X';
    int speed = 1;
    int health = 100;
    int rangeSight = 5;
    int rangeAttack = 1;
    int attack = 1;
    int armor = 5;
    Team team;
    int id;

    public enum Team {
        BLUE,
        RED
    }

    public char getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isAlly(BaseUnit unit) {
        return unit.team == this.team;
    }

    public int getId() {
        return id;
    }

    public boolean damageEntity(BaseUnit unit) {
        // TODO: Add math for damaging units according to our theme
        if (unit == null) {
            return false;
        }
        if (unit.attack - armor > 0) {
            health = health - (unit.attack - armor);
        }
        return health <= 0;
    }
}
