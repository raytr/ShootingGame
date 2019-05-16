package game.client;

import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;

public class GameCamera {
    private ParallelCamera pc;
    private Sprite centerSprite = new Sprite();
    private double screenWidth;
    private double screenHeight;

    public GameCamera() {
    }
    public void init(SubScene s,double screenWidth, double screenHeight){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        pc = new ParallelCamera();
        s.setCamera(pc);
    }

    //Binds the camera view to center on a sprite
    public void bindSprite(Sprite s) {
        centerSprite = s;
    }
    public Sprite getBoundedSprite(){return centerSprite;}

    public void update() {
        pc.setTranslateX(centerSprite.getX() + centerSprite.getWidth()/2 - screenWidth / 2);
        pc.setTranslateY(centerSprite.getY() + centerSprite.getHeight()/2- screenHeight / 2);
    }

    public double getX() {
        return pc.getTranslateX();
    }

    public double getY() {
        return pc.getTranslateY();
    }
}
