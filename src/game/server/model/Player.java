package game.server.model;

import game.shared.EntityType;

public class Player extends Entity {
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    public Player(int playerNum, String name) {
        this.name = name;
        this.id = playerNum;
        entityType = EntityType.PLAYER;
    }
    @Override
    public void update(){
        //System.out.println("ID: "+id + " X: " +x);
        int[] netMove = new int[]{0,0};

        if (moveUp) netMove[1] -= 1;
        if (moveDown) netMove[1] += 1;
        if (moveLeft) netMove[0] -= 1;
        if (moveRight) netMove[0] += 1;

        vx += netMove[0];
        vy += netMove[1];
        super.update();
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public String getName() {
        return name;
    }

}
