package game.server.model;

import game.shared.EntityType;

public abstract class Entity {
    protected String name = "";
    protected EntityType entityType;
    private static int entityCounter = 0;

    protected int id = 99 ;
    protected int updateTicks = 0;
    protected double x = 0;
    protected double y = 0;
    protected double vx = 0;
    protected double vy = 0;
    protected double width = 100;
    protected double height = 100;
    private double angle = 0;
    Entity(){
        entityCounter++;
        id += entityCounter;
    }

    public void update(){
        updateTicks++;
        x += vx;
        y += vy;
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

}
