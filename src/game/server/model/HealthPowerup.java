package game.server.model;

import game.shared.EntityType;

public class HealthPowerup extends Powerup{
    public HealthPowerup(){
        super();

    }
    @Override
    public void applyEffect(Player p) {
        p.setHp(p.getHp() + 50);

    }

    @Override
    public EntityType getEntityType() {
        return EntityType.HP_POWERUP;
    }
}
