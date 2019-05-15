package game.client;

import game.client.Sprite;

public class Player {
    private Sprite sprite;
    private final int playerId;
    private final String name;
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
}
