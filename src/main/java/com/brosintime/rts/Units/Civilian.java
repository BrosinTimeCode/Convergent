package com.brosintime.rts.Units;

import com.brosintime.rts.Model.Player.Team;

public class Civilian extends FootUnit {

    public Civilian(Team team, int id) {
        super.id = id;
        super.team = team;
        super.name = 'C';
        super.health = 100;
        super.attack = 5;
        super.armor = 5;
    }

}
