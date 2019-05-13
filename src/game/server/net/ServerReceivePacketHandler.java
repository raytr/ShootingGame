package game.server.net;

import game.server.GameServer;
import game.shared.net.ReceivePacketHandler;
import game.shared.net.packets.LoginMsg;
import game.shared.net.packets.LogoutMsg;
import game.shared.net.packets.Message;
import game.shared.net.packets.Packet;

public class ServerReceivePacketHandler implements ReceivePacketHandler {
    final GameServer gs;

    public ServerReceivePacketHandler(GameServer gs) {
        this.gs = gs;
    }

    @Override
    public void handleReceive(Packet packet) {
        for (Message m : packet.getMsgList()) {
            switch (m.getMsgType()) {
                case LOGIN:
                    String name = ((LoginMsg) m).getName();
                    //Generate a new player num for this player
                    int playerNum = gs.getNextPlayerNum();
                    System.out.println(name + " just logged in! Player num: " + playerNum);
                    //Add the new player to the game
                    gs.addPlayer(playerNum, name);
                    //Send to other clients!
                    //Make sure to add this as a send client before trying to expect an ack back
                    gs.getNetHandler().addSendClient(packet.getSocketAddress());
                    //Notify other players of the new player
                    gs.getNetHandler().sendMessage(LoginMsg.encode(name, playerNum));
                    break;
                case LOGOUT:
                    int playerNum2 = ((LogoutMsg) m).getPlayerNum();
                    System.out.println("SERVER-- CLIENT REQUEST LOGOUT " + playerNum2);
                    gs.removePlayer(playerNum2);
                    gs.getNetHandler().removeSendClient(packet.getSocketAddress());
                    //Notify other players of the logout
                    gs.getNetHandler().sendMessage(LogoutMsg.encode(playerNum2));
                    break;
            }
            System.out.println("SERVER RECEIVED " + m.getMsgType().toString());
        }
    }
}
