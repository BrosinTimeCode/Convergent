package Controller;

import Units.BaseUnit;
import java.util.TimerTask;

public class DamageEntityTask extends TimerTask {

    GameController controller;

    public DamageEntityTask(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.getEntitiesUnderAttack().forEach((k, v) -> {
            boolean deadUnit = v.damageEntity(k);
            if (deadUnit) {
                controller.killUnit(k, v);
            }
        });
    }
}
