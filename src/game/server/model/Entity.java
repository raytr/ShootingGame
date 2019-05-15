package game.server.model;

import game.shared.EntityType;

public abstract class Entity {
    protected String name;
    protected EntityType entityType;
    protected int id;
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected double width = 100;
    protected double height = 100;

    public EntityType getEntityType(){return entityType;};
    public void update(){
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
}
