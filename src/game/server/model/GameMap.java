package game.server.model;

import game.shared.EntityType;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private List<Entity> entities;
    private List<Wall> walls = new ArrayList<Wall>();
    private double width;
    private double height;
    public List<Wall> getWalls(){
        return walls;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public GameMap(List<Entity> entities, double width, double height){
        this.width = width;
        this.height = height;
        this.entities = entities;
        for (Entity e : entities){
            if (e.getEntityType().equals(EntityType.WALL)){
                walls.add((Wall) e);
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
