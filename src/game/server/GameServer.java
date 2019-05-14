package game.server;

import game.server.model.Entity;
import game.shared.GameLoop;
import game.server.model.Player;
import game.server.net.ServerReceivePacketHandler;
import game.shared.net.NetManager;

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
                for (Entity e : entityList){
                    e.update();
                }
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
        entityList.add(p);
    }
    public Collection<Player> getPlayerList(){
        return playerNumPlayerMap.values();
    }

    public void removePlayer(int playerNum) {
        playerNumPlayerMap.remove(playerNum);
        for (int i = entityList.size() -1;i>=0;i--) {
            if (entityList.get(i).getId() == playerNum){
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
}
