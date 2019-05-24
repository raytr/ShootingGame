package game.server.model;

import game.shared.EntityType;

import java.util.Timer;

public class ShotPowerup extends Powerup{

    @Override
    public void applyEffect(Player p) {
        if (p.getShootCooldownUpdateTicks() == 10) {
            p.setShootCooldownUpdateTicks(p.getShootCooldownUpdateTicks() - 5);

            Timer t = new java.util.Timer();
            t.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            p.setShootCooldownUpdateTicks(p.getShootCooldownUpdateTicks() + 5);
                            // your code here
                            // close the thread
                            t.cancel();
                        }
                    },
                    4000
            );
        }
    }
    @Override
    public EntityType getEntityType() {
        return EntityType.SHOT_POWERUP;
    }
}
