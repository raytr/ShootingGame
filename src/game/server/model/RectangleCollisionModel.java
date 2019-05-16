package game.server.model;


public class RectangleCollisionModel implements CollisionModel{
    Entity e;
    public RectangleCollisionModel(Entity e){
        this.e = e;
    }
    @Override
    public boolean collidesWith(CollisionModel model) {
        switch (model.getCollisionModelType()){
            case RECTANGLE:
                RectangleCollisionModel other = (RectangleCollisionModel) model;
                return (this.getX() < other.getX() + other.getWidth() &&
                        this.getX() + this.getWidth() > other.getX() &&
                        this.getY() < other.getY() + other.getHeight() &&
                        this.getY() + this.getHeight() > other.getY());
            case CIRCLE:
                CircleCollisionModel ccm = (CircleCollisionModel) model;
                double dx = ccm.getX() - Math.max(this.getX(), Math.min(ccm.getX(), this.getX() + this.getWidth()));
                double dy = ccm.getY() - Math.max(this.getY(), Math.min(ccm.getY(), this.getY() + this.getHeight()));
                return (dx * dx + dy * dy) < (ccm.getR() * ccm.getR());
            default:
                return false;
        }
    }

    @Override
    public CollisionModelType getCollisionModelType() {
        return CollisionModelType.RECTANGLE;
    }
    public double getX(){
        return e.getX();
    }
    public double getY(){
        return e.getY();
    }
    public double getWidth(){
        return e.getWidth();
    }
    public double getHeight(){
        return e.getHeight();
    }
}
