package Controller;

import Units.BaseUnit;
import java.util.TimerTask;

/**
 * The DamageEntityTask class is used to periodically damage entities that are under attack by other
 * entities. This class extends TimerTask and the time set for running this task is the rate at
 * which entities will be damaged.
 */
public class DamageEntityTask extends TimerTask {

    GameController controller;

    public DamageEntityTask(GameController controller) {
        this.controller = controller;
    }

    /**
     * Runs a periodic call to the entities damage method to damage each entity under attack.
     */
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
