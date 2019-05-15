package game.client.net;

import game.client.Game;
import game.client.Player;
import game.client.Sprite;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.messages.*;
import game.shared.net.Message;
import game.shared.net.Packet;

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
                    Player p = g.addPlayer(playerNum, name);
                    if (name.equals(g.getPlayerName()) && g.getPlayerNum() == -1) {
                        System.out.println("CLIENT - Player assigned playerNum " + playerNum);
                        g.setPlayerNum(playerNum);
                        g.setClientPlayer(p);
                    }
                    if (g.getChatBox() != null) g.getChatBox().addMsg(playerNum,
                            "<Player: " + playerNum + " Name: " + name + " > logged in!", true);
                    break;
                case LOGOUT:
                    playerNum = ((LogoutMsg) m).getPlayerNum();
                    g.getChatBox().addMsg(playerNum,
                            "<Player: " + playerNum + " Name: " + g.getPlayer(playerNum).getName() + " > logged out!",
                            true);
                    g.removePlayer(playerNum);
                    break;
                case CHAT_MESSAGE:
                    playerNum = ((ChatMsg) m).getPlayerNum();
                    String msg = ((ChatMsg) m).getMsg();
                    g.getChatBox().addMsg(playerNum, msg, false);
                    break;
                case ENTITY_CREATE:
                    EntityCreateMsg ecm = (EntityCreateMsg) m;
                    Sprite newSprite = new Sprite();
                    newSprite.setId(ecm.getId());
                    newSprite.setName(ecm.getName());
                    g.getPlayfield().addSprite(newSprite);

                    break;

                case ENTITY_DELETE:
                    EntityDeleteMsg edm = (EntityDeleteMsg) m;
                    g.getPlayfield().removeSpriteById(edm.getId());
                    break;
                case ENTITY_STATE:
                    EntityStateMsg esm = (EntityStateMsg) m;
                    int id = esm.getId();
                    Sprite sprite = g.getPlayfield().getSpriteById(id);
                    if (sprite != null) {
                        //sprite.setX(esm.getX());
                        //sprite.setY(esm.getY());
                        sprite.setRemoteX(esm.getX());
                        sprite.setRemoteY(esm.getY());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
