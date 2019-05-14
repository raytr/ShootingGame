package game.client.net;

import game.client.Game;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.messages.ChatMsg;
import game.shared.net.messages.LoginMsg;
import game.shared.net.Message;
import game.shared.net.Packet;
import game.shared.net.messages.LogoutMsg;

public class ClientReceivePacketHandler implements ReceivePacketHandler {
    final Game g;

    public ClientReceivePacketHandler(Game g) {
        this.g = g;
    }

    @Override
    public void handleReceive(Packet packet) {
        int playerNum = -1;
        for (Message m : packet.getMsgList()) {
            switch (m.getMsgType()) {
                case LOGIN:
                    LoginMsg lm = (LoginMsg) m;
                    String name = lm.getName();
                    playerNum = lm.getPlayerNum();
                    //Server assigned us a player num!
                    if (name.equals(g.getPlayerName()) && g.getPlayerNum() == -1) {
                        System.out.println("CLIENT - Player assigned playerNum " + playerNum);
                        g.setPlayerNum(playerNum);
                    }
                    g.addPlayer(playerNum,name);
                    if (g.getChatBox() != null) g.getChatBox().addMsg(playerNum,
                            "<Player: "+playerNum +" Name: " + name+" > logged in!",true);
                    break;
                case LOGOUT:
                    playerNum = ((LogoutMsg) m).getPlayerNum();
                    g.getChatBox().addMsg(playerNum,
                            "<Player: "+playerNum +" Name: " + g.getPlayer(playerNum).getName() +" > logged out!",
                            true);
                    g.removePlayer(playerNum);
                    break;
                case CHAT_MESSAGE:
                    playerNum = ((ChatMsg)m).getPlayerNum();
                    String msg = ((ChatMsg)m).getMsg();
                    g.getChatBox().addMsg(playerNum,msg,false);
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
