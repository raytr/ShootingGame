package game.server;

import game.client.Game;
import game.server.model.Entity;
import game.server.model.GameMap;
import game.server.model.Wall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapReader {
    private static final String DELIMITER = "";
    public static GameMap read(File mapCSVFile) {
        double pxPerIndex = new Wall().getWidth();
        List<Entity> entityList = new ArrayList<Entity>();
        int highestNumCols = -1;
        int rowCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(mapCSVFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                if (values.length > highestNumCols) highestNumCols = values.length;
                for (int col=0;col<values.length;col++){
                    Entity e = char2Entity(values[col].charAt(0),rowCount,col,pxPerIndex);
                    if(e != null)entityList.add(e);
                }
                rowCount++;
            }
        } catch (Exception e){

        }
        return new GameMap(entityList,highestNumCols * pxPerIndex,rowCount * pxPerIndex);
    }

    private static Entity char2Entity(char s,int rowIndex,int colIndex,double pxPerIndex){
        switch (s){
            case 'w':
                Wall w = new Wall();
                w.setX(colIndex * pxPerIndex);
                w.setY(rowIndex * pxPerIndex);
                System.out.println("X: "+w.getX());
                return w;
            default:
                return null;
        }
    }

}
