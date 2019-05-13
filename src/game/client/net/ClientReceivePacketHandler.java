package game.client.net;

import game.client.Game;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.packets.LoginMsg;
import game.shared.net.packets.Message;
import game.shared.net.packets.Packet;

public class ClientReceivePacketHandler implements ReceivePacketHandler {
    final Game g;

    public ClientReceivePacketHandler(Game g) {
        this.g = g;
    }

    @Override
    public void handleReceive(Packet packet) {
        for (Message m : packet.getMsgList()) {
            switch (m.getMsgType()) {
                case LOGIN:
                    LoginMsg lm = (LoginMsg) m;
                    String name = lm.getName();
                    int playerNum = lm.getPlayerNum();
                    //Server assigned us a player num!
                    if (name.equals(g.getPlayerName()) && g.getPlayerNum() == -1) {
                        System.out.println("CLIENT - Player assigned playerNum " + playerNum);
                        g.setPlayerNum(playerNum);
                    }
                    break;
                case LOGOUT:
                    break;
                case CONTROLS:
                    break;
                default:
                    break;
            }

            System.out.println("CLIENT RECEIVED " + m.getMsgType().toString());
        }
    }
}
