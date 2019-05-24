package game.server.model;

import game.shared.EntityType;

public abstract class Powerup extends Entity{
    Powerup(){
       super();
       width = 50;
       height = 50;
    }
    public abstract void applyEffect(Player p);
}
