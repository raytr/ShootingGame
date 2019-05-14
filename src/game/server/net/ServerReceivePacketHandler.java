package game.server.net;

import game.server.GameServer;
import game.server.model.Player;
import game.shared.net.NetManager;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.messages.ChatMsg;
import game.shared.net.messages.LoginMsg;
import game.shared.net.messages.LogoutMsg;
import game.shared.net.Message;
import game.shared.net.Packet;

public class ServerReceivePacketHandler implements ReceivePacketHandler {
    private final GameServer gs;

    public ServerReceivePacketHandler(GameServer gs) {
        this.gs = gs;
    }

    @Override
    public void handleReceive(Packet packet) {
        int playerNum = -1;
        for (Message m : packet.getMsgList()) {
            switch (m.getMsgType()) {
                case LOGIN:
                    String name = ((LoginMsg) m).getName();
                    //Generate a new player num for this player
                    playerNum = gs.getNextPlayerNum();
                    System.out.println(name + " just logged in! Player num: " + playerNum);
                    //Add the new player to the game
                    gs.addPlayer(playerNum, name);
                    //Send to other clients!
                    //Make sure to add this as a send client before trying to expect an ack back
                    gs.getNetManager().addSendClient(packet.getSocketAddress());
                    //Notify other players of the new player
                    gs.getNetManager().sendMessage(LoginMsg.encode(name, playerNum));
                    //Notify the new player about old players
                    for (Player p : gs.getPlayerList()){
                        gs.getNetManager().sendMessage(
                                LoginMsg.encode(p.getName(),p.getId()),
                                packet.getSocketAddress());
                    }
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

            }
            //System.out.println("SERVER RECEIVED " + m.getMsgType().toString());
        }
    }
}
