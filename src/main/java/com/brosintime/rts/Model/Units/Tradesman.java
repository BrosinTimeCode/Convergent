package com.brosintime.rts.Model.Units;

import com.brosintime.rts.Model.Player.Team;

public class Tradesman extends FootUnit {

    public Tradesman(Team team, int id) {
        this.id = id;
        this.team = team;
        this.name = 'T';
        this.longName = "Tradesman";
        this.health = 100;
        this.attack = 3;
        this.armor = 3;
    }
}
