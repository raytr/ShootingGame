package game.client;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Sprite {
    private final double INTERP_CONSTANT = 0.95;
    private String name = "test";
    private Color color = Color.rgb(255,0,0,0.2);
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
    private double width = 70;
    private double height = 70;
    private double angle = 0;
    private double remoteAngle = 0;
    private int hp= 0;

    public String getName(){
        return name;
    }
    public int getHp(){return hp;}
    public void setHp(int h){hp=h;}
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
        //Set rotation
        gc.save();
        //Draw body
        //Affine affine = new Affine();
        //affine.appendTranslation(x + width/2,y + height/2);
        //affine.appendRotation(Math.toDegrees(angle));
        //gc.transform(affine);

        gc.translate(x +width/2,y + width/2);
        gc.rotate(Math.toDegrees(angle));

        gc.setFill(color);
        gc.fillRect(-width/2, -height/2, width, height);
        //Draw name
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.rotate(-Math.toDegrees(angle));
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Arial", 24));
        gc.fillText(name,0,0);
        //Draw health bar
        gc.setFill(Color.GREEN);
        gc.fillRect(-width/2,-height/2 - 30,width * hp/100,height/2 - 20);

        //Draw network shadow
        gc.translate(-x -width/2 ,-y - width/2);
        gc.translate(remoteX +width/2,remoteY +width/2);
        gc.rotate(Math.toDegrees(remoteAngle));
        gc.setFill(Color.rgb(0, 0, 255, 0.2));
        gc.fillRect(-width/2, -height/2, width, height);


        //Restrore gc
        gc.restore();
    }
    public void setName(String s){
        this.name =s;

    }

    public void update(double dt) {
        x = interpolate(x,remoteX,0.001,500);
        //System.out.println("XF: "+x);
        y = interpolate(y,remoteY,0.001,500);
        angle = interpolate(angle,remoteAngle,0.001,Math.PI);

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
    }
    private double interpolate(double x0,double xf,double minThreshold,double maxThreshold){
        //System.out.println(dt);
        double difference = xf - x0;
        if (Math.abs(difference) < minThreshold || Math.abs(difference) > maxThreshold)
            return xf;
        else {
            return x0 + difference * INTERP_CONSTANT;
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

    public void setRotation(float angle) {
        this.angle = angle;
    }

    public void setRemoteAngle(float angle) {
        this.remoteAngle = angle;
    }
    public void setRGBColor(int r,int g, int b,int opacity){
        this.color = new Color(r,g,b,opacity);
    }
}
