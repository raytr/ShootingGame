package game.server;

import game.server.model.Player;
import game.server.net.ServerReceivePacketHandler;
import game.shared.net.NetHandler;

import java.util.HashMap;
import java.util.Map;

public class GameServer {
    private NetHandler nh;
    private int playerNumCounter = 0;
    private final int port;
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
        nh = new NetHandler(srph, port);
    }

    public NetHandler getNetHandler() {
        return nh;
    }

    public void addPlayer(int playerNum, String name) {
        Player p = new Player(playerNum, name);
        playerNumPlayerMap.put(playerNum, p);
    }

    public void removePlayer(int playerNum) {
        playerNumPlayerMap.remove(playerNum);
    }

    public void stop() {
        System.out.println("Closing Server");
        nh.stop();
    }

    public int getNextPlayerNum() {
        playerNumCounter++;
        return playerNumCounter;
    }
}
