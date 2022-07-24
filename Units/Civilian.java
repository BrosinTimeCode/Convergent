package Units;

public class Civilian extends FootUnit {
    public Civilian(Team team) {
        name = 'T';
        speed = 2;
        health = 100;
        rangeSight = 4;
        rangeAttack = 2;
        attack = 5;
        armor = 5;
        rangeAct = 1;
        team = team;
    }
}
