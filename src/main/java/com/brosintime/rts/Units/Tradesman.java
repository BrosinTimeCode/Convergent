package com.brosintime.rts.Units;

public class Tradesman extends FootUnit {

    public Tradesman(Team team, int id) {
        super.id = id;
        super.team = team;
        super.name = 'T';
        super.health = 100;
        super.attack = 3;
        super.armor = 3;
    }
}
