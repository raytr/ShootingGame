package game.client.net;

import game.client.Game;
import game.client.Player;
import game.client.Sprite;
import game.client.SpriteFactory;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.messages.*;
import game.shared.net.Message;
import game.shared.net.Packet;
import javafx.application.Platform;

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
                    g.getPlayfield().addSprite(SpriteFactory.createSprite(ecm));

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
                        sprite.setHp(esm.getHp());
                        sprite.setRemoteX(esm.getX());
                        sprite.setRemoteY(esm.getY());
                        sprite.setRemoteAngle(esm.getAngle());
                    }
                    break;
                case GAME_MAP:
                    GameMapMsg gmm = (GameMapMsg)m;
                    g.getPlayfield().setCanvasWidth(gmm.getWidth());
                    g.getPlayfield().setCanvasHeight(gmm.getHeight());
                    break;
                case PLAYER_STATE:
                    PlayerStateMsg psm = (PlayerStateMsg)m;
                    Player player = g.getPlayer(psm.getPlayerNum());
                    player.setScore(psm.getScore());

                    break;
                default:
                    break;
            }
        }
    }
}
