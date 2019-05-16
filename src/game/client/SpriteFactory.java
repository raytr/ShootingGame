package game.client;

import game.shared.net.messages.EntityCreateMsg;

public class SpriteFactory {
    public static Sprite createSprite(EntityCreateMsg ecm){
        Sprite newSprite = new Sprite();
        newSprite.setRemoteX(ecm.getX0());
        newSprite.setRemoteY(ecm.getY0());
        newSprite.setX(ecm.getX0());
        newSprite.setY(ecm.getY0());
        newSprite.setId(ecm.getId());
        newSprite.setName(ecm.getName());

        switch (ecm.getEntityType()){
            case PLAYER:
                break;
            case BULLET:
                newSprite.setWidth(10);
                newSprite.setHeight(10);
                break;
        }
        return newSprite;
    }
}
