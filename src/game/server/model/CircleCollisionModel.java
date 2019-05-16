package game.server.model;

public class CircleCollisionModel implements CollisionModel{
    private Entity e;
    private double r;
    public CircleCollisionModel(Entity parent){
        this.e = parent;
        r = Math.max(e.getWidth()/2,e.getHeight()/2);
    }
    @Override
    public boolean collidesWith(CollisionModel model) {
        switch (model.getCollisionModelType()){
            case CIRCLE:
                CircleCollisionModel ccm = (CircleCollisionModel)model;
                double xDiff = this.getX() - ccm.getX();
                double yDiff = this.getY() - ccm.getY();
                double rSquared = xDiff * xDiff + yDiff * yDiff;
                return rSquared < (this.getR() + ccm.getR()) * (this.getR() + ccm.getR());
            case RECTANGLE:
                RectangleCollisionModel rcm = (RectangleCollisionModel)model;
                double dx = this.getX() - Math.max(rcm.getX(), Math.min(this.getX(), rcm.getX() + rcm.getWidth()));
                double dy = this.getY() - Math.max(rcm.getY(), Math.min(this.getY(), rcm.getY() + rcm.getHeight()));
                return (dx * dx + dy * dy) < (this.getR() * this.getR());
            default:
                return false;
        }
    }

    @Override
    public CollisionModelType getCollisionModelType() {
        return CollisionModelType.CIRCLE;
    }
    //Returns the center of the circle
    public double getX(){
        return e.getX() + e.getWidth()/2;
    }
    public double getY(){
        return e.getY() + e.getHeight()/2;
    }
    public double getR(){
        return r;
    }
}
