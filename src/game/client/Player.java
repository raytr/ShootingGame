package game.client;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player {
    private Sprite sprite;
    private final int playerId;
    private final String name;
    private final IntegerProperty score = new SimpleIntegerProperty();


    public Player(int playerId, String name){
        this.playerId = playerId;
        this.name = name;
    }
    public int getPlayerId(){
        return playerId;
    }
    public String getName(){
        return name;
    }
    public void setSprite(Sprite s){
        this.sprite = s;
    }
    public Sprite getSprite(){return sprite;}
    public int getScore(){
        return score.get();
    }

    public void setScore(int i){
        score.set(i);
    }

    public IntegerProperty scoreProperty(){
        return score;
    }
}
