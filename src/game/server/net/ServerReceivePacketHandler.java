package game.server.net;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import game.server.GameServer;
import game.server.model.Entity;
import game.server.model.Player;
import game.shared.net.PacketSuccessFailHandler;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.messages.*;
import game.shared.net.Message;
import game.shared.net.Packet;
import game.shared.net.messages.commands.ChangeAngleCommand;
import game.shared.net.messages.commands.MoveCommand;
import game.shared.net.messages.commands.SetShootingCommand;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerReceivePacketHandler implements ReceivePacketHandler {
    private final GameServer gs;

    public ServerReceivePacketHandler(GameServer gs) {
        this.gs = gs;
    }

    @Override
    public void handleReceive(Packet packet) {
        int playerNum = -1;
        for (Message m : packet.getMsgList()) {

            //Make sure to add this as a send client before trying to expect an ack back
            gs.getNetManager().addSendClient(packet.getSocketAddress());
            switch (m.getMsgType()) {
                case LOGIN:
                    //Make sure to add this as a send client before trying to expect an ack back
                    //gs.getNetManager().addSendClient(packet.getSocketAddress());

                    String name = ((LoginMsg) m).getName();
                    //Generate a new player num for this player
                    playerNum = gs.getNextPlayerNum();
                    System.out.println(name + " just logged in! Player num: " + playerNum);
                    //Add the new player to the game
                    gs.addPlayer(playerNum, name);
                    //Send to other clients!
                    List<Message> newPlayerMessages = new ArrayList<Message>();
                    //Register the new player to all clients, then send the data that depends on playerNum
                    gs.getNetManager().sendMessage(LoginMsg.encode(name, playerNum), new PacketSuccessFailHandler() {
                        @Override
                        public void run(InetSocketAddress addr) {
                            //Notify the new player about old players
                            for (Player p : gs.getPlayerList()){
                                newPlayerMessages.add(LoginMsg.encode(p.getName(),p.getId()));
                            }
                            //Notify the new player about old entities
                            for (Entity e : gs.getEntityList()){
                                newPlayerMessages.add(EntityCreateMsg.encode(e.getId(),e.getEntityType(),e.getX(),e.getY(),e.getName()));
                            }
                            //Notify the new player about the map size
                            newPlayerMessages.add(GameMapMsg.encode(gs.getMapWidth(),gs.getMapHeight()));
                            //Notify the new player about player states
                            for (Player p : gs.getPlayerList()) {
                                newPlayerMessages.add(PlayerStateMsg.encode(p.getId(),p.getScore()));
                            }
                            //Send all messages
                            gs.getNetManager().sendMessages(newPlayerMessages,packet.getSocketAddress());

                        }
                    }, new PacketSuccessFailHandler() {
                        @Override
                        public void run(InetSocketAddress addr) {
                        }
                    });



                    break;
                case LOGOUT:
                    playerNum = ((LogoutMsg) m).getPlayerNum();
                    System.out.println("SERVER-- CLIENT REQUEST LOGOUT " + playerNum);
                    gs.removePlayer(playerNum);
                    gs.getNetManager().removeSendClient(packet.getSocketAddress());
                    //Notify other players of the logout
                    gs.getNetManager().sendMessage(LogoutMsg.encode(playerNum));
                    break;
                case CHAT_MESSAGE:
                    playerNum = ((ChatMsg)m).getPlayerNum();
                    String msg = ((ChatMsg)m).getMsg();
                    //Notify players of the message
                    gs.getNetManager().sendMessage(ChatMsg.encode(msg,playerNum));
                    break;
                case PLAYER_COMMAND:
                    Command c = ((CommandMsg)m).getCommand();
                    //System.out.println(c.getType());
                    Player p = gs.getPlayerById(((CommandMsg) m).getPlayerNum());
                    switch (c.getType()){
                        case MOVE:
                            MoveCommand mc = (MoveCommand)c;
                            if(mc.getMoveDown() != null) p.setMoveDown(mc.getMoveDown());
                            if(mc.getMoveUp() != null) p.setMoveUp(mc.getMoveUp());
                            if(mc.getMoveLeft() != null) p.setMoveLeft(mc.getMoveLeft());
                            if(mc.getMoveRight() !=null ) p.setMoveRight(mc.getMoveRight());
                            break;
                        case CHANGE_ANGLE:
                            ChangeAngleCommand mmc = (ChangeAngleCommand)c;
                            p.setAngle(mmc.getAngle());
                            break;
                        case SET_SHOOTING:
                            SetShootingCommand ssc = (SetShootingCommand)c;
                            p.setIsShooting(ssc.getIsShooting());
                            break;
                    }
                    break;

            }
            //System.out.println("SERVER RECEIVED " + m.getMsgType().toString());
        }
    }
}
