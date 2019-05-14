package game.client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class GameArea {
    public Canvas gameArea;
    GraphicsContext gc;
    public GameArea(int width, int height){
        Canvas canvas = new Canvas(width,height);
        gc = canvas.getGraphicsContext2D();
    }
    public Canvas getBody(){
        return gameArea;
    }
    private void clear(){
        gc.clearRect(0, 0, gameArea.getWidth(), gameArea.getHeight());
    }
}
