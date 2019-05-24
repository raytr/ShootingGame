package game.client;

import game.shared.GameLoop;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;


//Canvas element for the game
//Draws sprites
public class Playfield {
    private Node body;
    private Canvas gameArea;
    private GraphicsContext gc;
    private ConcurrentHashMap<Integer,Sprite> spriteMap = new ConcurrentHashMap<Integer,Sprite>();
    private GameCamera gameCamera = new GameCamera();
    private double width;
    private double height;
    private double canvasWidth = 1000;
    private double canvasHeight = 1000;
    private boolean drawWinMsg = false;
    private String winningPlayer;
    private Game g;
    public Playfield(Game g){
        this.g = g;
    }
    public void init(double width, double height){
        this.width = width;
        this.height = height;
        gameArea = new Canvas(canvasWidth,canvasHeight);
        gameArea.setFocusTraversable(true);
        gc = gameArea.getGraphicsContext2D();


        VBox vbox2 = new VBox();
        vbox2.getChildren().add(gameArea);
        SubScene subScene = new SubScene(vbox2,width,height);
        gameCamera.init(subScene,width,height);
        body = subScene;

    }
    public void bindCameraToSprite(Sprite s){
        gameCamera.bindSprite(s);
    }
    public Node getBody(){
        return body;
    }
    private void clear(){
        gc.clearRect(0, 0, gameArea.getWidth(), gameArea.getHeight());
    }
    public void draw(){
        if (canvasWidth != gameArea.getWidth()) gameArea.setWidth(canvasWidth);
        if (canvasHeight != gameArea.getHeight()) gameArea.setHeight(canvasHeight);
        clear();
        for (Sprite s : spriteMap.values()){
            if (gameCamera.isVisible(s.getX(),s.getY(),s.getWidth(),s.getHeight())){
                s.draw(gc);
            }
        }
        createBorder();
        if (drawWinMsg){
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.DARKORANGE);
            gc.setFont(Font.font ("Arial", 64));
            gc.fillText(winningPlayer + " WON THE ROUND!",
                    gameCamera.getScreenWidth()/2 + gameCamera.getX(),
                    gameCamera.getScreenHeight()/2 + gameCamera.getY() - 100);
        }
    }

    private void createBorder() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,1,gameArea.getHeight());
        gc.fillRect(0,0,gameArea.getWidth(),1);
        gc.fillRect(gameArea.getWidth()-1,0,1,gameArea.getHeight());
        gc.fillRect(0,gameArea.getHeight()-1,gameArea.getWidth(),1);
    }

    public void addSprite(Sprite s){
        spriteMap.put(s.getId(),s);
    }
    public Sprite getSpriteById(int id){
        return spriteMap.get(id);
    }
    public void removeSpriteById(int id){
        spriteMap.remove(id);
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public List<Sprite> getSpriteList() {
        return new ArrayList<>(spriteMap.values());
    }


    public Sprite getCameraBoundedSprite() {
        return gameCamera.getBoundedSprite();
    }

    public double getHeight() {
        return height;
    }
    public double getWidth(){
        return width;
    }

    public void setCanvasHeight(float height) {
        canvasHeight =height;
    }

    public void setCanvasWidth(float width) {
        canvasWidth = width;
    }

    public void displayWinMsg(Player p) {
        drawWinMsg = true;
        winningPlayer = p.getName();
        Timer t = new java.util.Timer();
            t.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // close the thread
                            drawWinMsg = false;
                            t.cancel();
                        }
                    },
                    5000
            );
    }
}
