package game.server.model;

import game.shared.EntityType;

import java.util.Collection;

public abstract class Entity {
    protected String name = "";
    protected EntityType entityType;
    private static int entityCounter = 0;
    //Whether or not to send updates
    protected boolean sendUpdates = true;
    protected CollisionModel collisionModel = new RectangleCollisionModel(this);

    protected int hp = 0;
    protected int id = 99 ;
    protected int updateTicks = 0;
    protected double x = 0;
    protected double y = 0;
    protected double vx = 0;
    protected double vy = 0;
    protected double width = 70;
    protected double height = 70;
    private double angle = 0;
    private int score = 0;

    Entity(){
        entityCounter++;
        id += entityCounter;
    }

    public boolean getSendUpdates(){
        return sendUpdates;
    }
    public void updateVelocity(){
        updateTicks++;
        vx*=0.9;
        vy*=0.9;
    }
    public String getName(){return name;}
    public int getId(){
        return id;
    }
    public void setId(int newID){
        id = newID;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAngle(double angle){
        this.angle = angle;

    };
    public double getAngle(){
        return angle;
    }
    public abstract EntityType getEntityType();

    public int getHp(){
        return hp;
    };
    public void setHp(int h){
        hp = h;
        if (hp < 0) hp =0;
    }
    public boolean collides(Entity other){
        return collisionModel.collidesWith(other.getCollisionModel());
    }
    public boolean collides(Collection<? extends Entity> entities){
        for (Entity e : entities){
            if (collisionModel.collidesWith(e.getCollisionModel())){
                return true;
            }
        }
        return false;
    }

    protected CollisionModel getCollisionModel(){return collisionModel;}

    public void setScore(int i){
        this.score = i;
    }
    public int getScore(){
        return score;

    }
}
