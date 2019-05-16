package game.server.model;

import game.shared.EntityType;

public class Bullet extends Entity{
    public Bullet(){
        super();
        width=10;
        height=10;
        vx = 70;
        vy = 70;

    }

    @Override
    public EntityType getEntityType() {
        return EntityType.BULLET;
    }
}
