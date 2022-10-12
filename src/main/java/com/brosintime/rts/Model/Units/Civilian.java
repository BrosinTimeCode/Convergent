package com.brosintime.rts.Model.Units;

import com.brosintime.rts.Model.Player.Team;

public class Civilian extends FootUnit {

    public Civilian(Team team, int id) {
        this.id = id;
        this.team = team;
        this.name = 'C';
        this.longName = "Civilian";
        this.health = 100;
        this.attack = 5;
        this.armor = 5;
    }

}
