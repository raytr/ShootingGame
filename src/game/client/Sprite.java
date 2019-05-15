package game.client;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Sprite {
    private final double INTERP_CONSTANT = 0.9;
    private String name = "test";
    private int id;
    private boolean goUp = false;
    private boolean goDown = false;
    private boolean goRight = false;
    private boolean goLeft = false;
    private double remoteX = 0;
    private double remoteY = 0;
    private double x = 0;
    private double y = 0;
    private double vx = 0;
    private double vy = 0;
    private double width = 100;
    private double height = 100;
    public String getName(){
        return name;
    }
    public int getId(){return id;}
    public void setId(int i){id =i;}
    public void setRemoteX(double x2){
        remoteX = x2;
    }
    public void setRemoteY(double y2){
        remoteY = y2;
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

    void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x, y, width, height);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Arial", 24));
        gc.fillText(name,x,y-10);
    }
    public void setName(String s){
        this.name =s;

    }

    public void update(double dt) {
        /*
        int[] netMove = new int[]{0,0};

        if (goUp) netMove[1] -= 1;
        if (goDown) netMove[1] += 1;
        if (goLeft) netMove[0] -= 1;
        if (goRight) netMove[0] += 1;

        vx += netMove[0];
        vy += netMove[1];

        x+=vx;
        y+=vy;

        vx*=0.9;
        vy*=0.9;
         */
        System.out.println("X0: "+x);
        x = interpolate(dt,x,remoteX,0.02,50);
        System.out.println("XF: "+x);
        y = interpolate(dt,y,remoteY,0.02,50);
    }
    private double interpolate(double dt,double x0,double xf,double minThreshold,double maxThreshold){
        System.out.println(dt);
        double difference = xf - x0;
        if (difference < minThreshold || difference > maxThreshold)
            return xf;
        else {
            System.out.println("INTERPOLATING");
            return x0 + difference * dt * (1000/60) ;
        }

    }

    public void setGoRight(boolean goRight) {
        this.goRight = goRight;
    }
    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }
    public void setGoUp(boolean goUp) {
        this.goUp = goUp;
    }
    public void setGoDown(boolean goDown) {
        this.goDown = goDown;
    }
}
