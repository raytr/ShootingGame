package game.server.model;

public class Player extends Entity {
    private final String name;

    public Player(int playerNum, String name) {
        this.name = name;
        this.id = playerNum;
    }

    public String getName() {
        return name;
    }

}
