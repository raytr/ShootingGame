package game.client.model;

public class Player {
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
}
