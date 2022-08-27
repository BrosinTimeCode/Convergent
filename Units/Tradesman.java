package Units;

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
}
