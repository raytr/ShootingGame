package game.client;

import game.shared.net.messages.EntityCreateMsg;
import javafx.scene.paint.Color;

public class SpriteFactory {
    public static Sprite createSprite(EntityCreateMsg ecm){
        Sprite newSprite = new Sprite(ecm.getEntityType());
        newSprite.setRemoteX(ecm.getX0());
        newSprite.setRemoteY(ecm.getY0());
        newSprite.setX(ecm.getX0());
        newSprite.setY(ecm.getY0());
        newSprite.setId(ecm.getId());
        newSprite.setName(ecm.getName());

        switch (ecm.getEntityType()){
            case PLAYER:
                newSprite.setWidth(70);
                newSprite.setHeight(70);
                break;
            case BULLET:
                newSprite.setWidth(10);
                newSprite.setHeight(10);
                break;
            case WALL:
                newSprite.setRGBColor(0,0,0,1);
                newSprite.setWidth(50);
                newSprite.setHeight(50);
                break;
            case HP_POWERUP:
                newSprite.setRGBColor(0,1,0,1);
                newSprite.setName("HP");
                newSprite.setWidth(50);
                newSprite.setHeight(50);
                break;
            case SHOT_POWERUP:
                newSprite.setRGBColor(0,0,1,0.4);
                newSprite.setName("SHOT++");
                newSprite.setWidth(50);
                newSprite.setHeight(50);
                break;
            default:
                break;
        }
        return newSprite;
    }
}
