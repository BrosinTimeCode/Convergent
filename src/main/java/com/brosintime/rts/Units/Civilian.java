package com.brosintime.rts.Units;

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
}
