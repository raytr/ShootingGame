package game.server.model;

public interface CollisionModel {
    boolean collidesWith(CollisionModel model);
    CollisionModelType getCollisionModelType();
}
