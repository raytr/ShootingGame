package game.server.model;

public class Player {
    private final int playerNum;
    private final String name;

    public Player(int playerNum, String name) {
        this.name = name;
        this.playerNum = playerNum;
    }

    public String getName() {
        return name;
    }

    public int getPlayerNum() {
        return playerNum;
    }
}
