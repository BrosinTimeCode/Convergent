package Units;

public class Civilian extends FootUnit {

  // TODO: remove this or optimize where to put this information for determining unit faction
  public Civilian(Team team) {
    name = 'C';
    speed = 2;
    health = 100;
    rangeSight = 4;
    rangeAttack = 2;
    attack = 5;
    armor = 5;
    rangeAct = 1;
    super.team = team;
  }
}
