package game.server.model;

import game.shared.EntityType;

public class Bullet extends Entity{
    private int damage = 5;
    private Entity shotFrom;
    public Bullet(Entity shotFrom){
        super();
        this.shotFrom = shotFrom;
        width=10;
        height=10;
        vx = 40;
        vy = 40;
        collisionModel = new CircleCollisionModel(this);

    }
    public int getDamage(){return damage;}

    @Override
    public EntityType getEntityType() {
        return EntityType.BULLET;
    }

    public Entity getShotFrom() {
        return shotFrom;
    }
}
