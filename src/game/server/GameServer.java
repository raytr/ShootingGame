package game.server;

import game.server.model.Entity;
import game.shared.EntityType;
import game.shared.GameLoop;
import game.server.model.Player;
import game.server.net.ServerReceivePacketHandler;
import game.shared.net.Message;
import game.shared.net.NetManager;
import game.shared.net.messages.EntityCreateMsg;
import game.shared.net.messages.EntityDeleteMsg;
import game.shared.net.messages.EntityStateMsg;

import java.util.*;

public class GameServer {
    private NetManager nh;
    private GameLoop gl;
    private int playerNumCounter = 0;
    private final int port;
    private List<Entity> entityList = new ArrayList<>();
    private final Map<Integer, Player> playerNumPlayerMap = new HashMap<>();

    public GameServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer(4444);
        gs.init();
    }

    public void init() {
        ServerReceivePacketHandler srph = new ServerReceivePacketHandler(this);
        nh = new NetManager(srph, port);

        gl  = new GameLoop(new Runnable() {
            @Override
            public void run() {
                List<Message> entityStateMsgs = new ArrayList<Message>();
                for (Entity e : entityList){
                    e.update();
                    entityStateMsgs.add(EntityStateMsg.encode(e.getId(),e.getX(),e.getY(),e.getAngle()));
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
        nh.sendMessage(EntityCreateMsg.encode(playerNum, EntityType.PLAYER,p.getX(),p.getY(),name));
        entityList.add(p);
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
}
