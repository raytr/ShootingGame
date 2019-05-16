package game.server.model;

import game.server.MapReader;
import game.shared.EntityType;

public class Wall extends Entity {
    public Wall(){
        super();
        sendUpdates = false;
        height = 50;
        width = 50;
    }
    @Override
    public EntityType getEntityType() {
        return EntityType.WALL;
    }
}
