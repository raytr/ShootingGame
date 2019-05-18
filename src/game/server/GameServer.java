package game.server;

import game.client.Playfield;
import game.server.model.*;
import game.shared.EntityType;
import game.shared.GameLoop;
import game.server.net.ServerReceivePacketHandler;
import game.shared.net.Message;
import game.shared.net.NetManager;
import game.shared.net.messages.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
    private NetManager nh;
    private GameLoop gl;
    private int playerNumCounter = 0;
    private final int port;
    private List<Entity> entityList = new CopyOnWriteArrayList<>();
    private List<Entity> bulletList = new ArrayList<Entity>();
    private final Map<Integer, Player> playerNumPlayerMap = new HashMap<>();
    private GameMap gameMap = new GameMap(new ArrayList<Entity>(),2000,2000);

    public GameServer(int port) {
        this.port = port;
    }

    public void init() {
        ServerReceivePacketHandler srph = new ServerReceivePacketHandler(this);
        nh = new NetManager(srph, port);

        gl  = new GameLoop(new Runnable() {
            @Override
            public void run() {

                //Add new bullets
                List<Entity> newBullets = new ArrayList<Entity>();
                for (Player p : playerNumPlayerMap.values()){
                    if (p.canShoot()){
                        Bullet b = new Bullet(p);
                        b.setVx(b.getVx() * Math.cos(p.getAngle()));
                        b.setVy(b.getVy() * Math.sin(p.getAngle()));
                        b.setX(p.getX() + p.getWidth()/2 - b.getWidth()/2);
                        b.setY(p.getY() + p.getHeight()/2 - b.getHeight()/2);
                        newBullets.add(b);
                    }
                }

                addEntities(newBullets);
                bulletList.addAll(newBullets);

                List<Entity> toRemove = new ArrayList<Entity>();
                for (Player p : playerNumPlayerMap.values()){
                    for (Entity b : bulletList){
                        if (b.collides(p) && !((Bullet)b).getShotFrom().equals(p)){
                            p.setHp(p.getHp() - ((Bullet)b).getDamage());
                            toRemove.add(b);
                            if (p.getHp() == 0){
                                do {
                                    p.setX(Math.random() * gameMap.getWidth());
                                    p.setY(Math.random() * gameMap.getHeight());
                                }while(p.collides(gameMap.getWalls()));

                                p.setHp(100);
                                Entity shotFrom = ((Bullet) b).getShotFrom();
                                int prevScore = ((Bullet)b).getShotFrom().getScore();
                                ((Bullet) b).getShotFrom().setScore(prevScore + 1);
                                nh.sendMessage(PlayerStateMsg.encode(shotFrom.getId(),prevScore + 1));
                            }
                        }
                    }
                    if (p.getX() < 0) p.setX(0);
                    if (p.getY() < 0) p.setY(0);
                    if (p.getX() + p.getWidth() > gameMap.getWidth()) p.setX(gameMap.getWidth() - p.getWidth());
                    if (p.getY() + p.getHeight() > gameMap.getHeight()) p.setY(gameMap.getHeight() - p.getHeight());
                }
                //Remove old entities
                for (Entity b : bulletList){
                    if (Math.abs(b.getVx())<5 && Math.abs(b.getVy())< 5){
                        toRemove.add(b);
                    }
                }
                bulletList.removeAll(toRemove);
                //Send entity remove messages
                removeEntities(toRemove);




                //Send entity state messages
                List<Message> entityStateMsgs = new ArrayList<Message>();

                for (Entity e : entityList){
                    e.updateVelocity();
                    double x0 = e.getX();
                    double y0 = e.getY();
                    /*
                    1st move along the x-axis,
                            then check collisions;
                    if it hit something, move back and stop x-velocity.
                            Then move in Y-direction; if it hit something, move back and stop y-velocity.
                     */
                    e.setX(x0 + e.getVx());
                    if(gameMap !=null) {
                        if (e.getEntityType() != EntityType.WALL && e.collides(gameMap.getWalls())) {
                            e.setX(x0);
                            e.setVx(0);
                        }
                    }
                    e.setY(y0 + e.getVy());
                    if (gameMap != null) {
                        if (e.getEntityType() != EntityType.WALL && e.collides(gameMap.getWalls())){
                            e.setY(y0);
                            e.setVy(0);
                        }
                    }
                    if (e.getSendUpdates()){
                        entityStateMsgs.add(EntityStateMsg.encode(e.getId(),e.getHp(),e.getX(),e.getY(),e.getAngle()));
                    }
                }
                nh.sendMessages(entityStateMsgs);
            }
        },60);
        gl.start();
    }

    public NetManager getNetManager() {
        return nh;
    }

    public void addPlayer(int playerNum, String name) {
        Player p = new Player(playerNum, name);
        playerNumPlayerMap.put(playerNum, p);
        addEntity(p);
    }
    public Collection<Player> getPlayerList(){
        return playerNumPlayerMap.values();
    }
    public Player getPlayerById(int id){
        return playerNumPlayerMap.get(id);
    }

    public void removePlayer(int playerNum) {
        nh.sendMessage(EntityDeleteMsg.encode(playerNum));
        playerNumPlayerMap.remove(playerNum);
        for (int i = entityList.size() -1;i>=0;i--) {
            if (entityList.get(i).getId() == playerNum){
                Entity e = entityList.get(i);
                entityList.remove(i);
                break;
            }
        }
    }

    public void stop() {
        gl.stopRunning();
        System.out.println("Closing Server");
        nh.stop();
    }

    public int getNextPlayerNum() {
        playerNumCounter++;
        return playerNumCounter;
    }
    public List<Entity> getEntityList(){return entityList;}
    private void addEntity(Entity e){
        nh.sendMessage(EntityCreateMsg.encode(e.getId(), e.getEntityType(),e.getX(),e.getY(),e.getName()));
        entityList.add(e);
    }
    private void addEntities(List<Entity> entities){
        List<Message> entityMsgs = new ArrayList<Message>();
        for (Entity e : entities){
            entityMsgs.add(EntityCreateMsg.encode(e.getId(),e.getEntityType(),e.getX(),e.getY(),e.getName()));
        }
        nh.sendMessages(entityMsgs);
        entityList.addAll(entities);
    }
    private void removeEntities(List<Entity> entities){
        List<Message> entityMsgs = new ArrayList<Message>();
        for (Entity e : entities){
            entityMsgs.add(EntityDeleteMsg.encode(e.getId()));
        }
        nh.sendMessages(entityMsgs);
        entityList.removeAll(entities);
    }
    public void loadGameMapFile(File file){
        if (gameMap != null){
            removeEntities(gameMap.getEntities());
        }
        gameMap = MapReader.read(file);
        addEntities(gameMap.getEntities());
        System.out.println("GAME WIDTH: "+gameMap.getWidth()+ " HEGIHT:" +gameMap.getHeight());
        nh.sendMessage(GameMapMsg.encode(gameMap.getWidth(),gameMap.getHeight()));
    }
    public double getMapWidth(){
        return gameMap.getWidth();
    }
    public double getMapHeight(){
        return gameMap.getHeight();
    }
}
