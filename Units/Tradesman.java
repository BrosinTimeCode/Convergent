package Units;

public class Tradesman extends FootUnit {
    public Tradesman(Team team) {
        name = 'T';
        speed = 1;
        health = 100;
        rangeSight = 5;
        rangeAttack = 1;
        attack = 3;
        armor = 3;
        rangeAct = 1;
        team = team;
    }
}
