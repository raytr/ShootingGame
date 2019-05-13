package game.shared.net;

import game.shared.net.packets.*;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class PacketReader {
    public static Packet read(DatagramPacket rawData) {
        byte[] bytes = rawData.getData();

        //ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ByteBuffer bb = ByteBuffer.wrap(bytes);

        //Read the header!!
        int seqNum = bb.getInt();
        boolean isReliable = (bb.get() == (byte) 1);

        Packet returnPacket = new Packet(seqNum, isReliable);
        returnPacket.setSocketAddress(new InetSocketAddress(rawData.getAddress(), rawData.getPort()));
        //Read the content
        while (bb.position() + Message.HEADER_BYTE_SIZE < rawData.getLength()) {
            MsgType msgType = MsgType.valueOf(bb.getInt());
            int size = bb.getInt();
            byte[] msgBytes = new byte[size];
            bb.get(msgBytes, 0, size);

            switch (msgType) {
                case ACK:
                    returnPacket.addMessage(AckMsg.decode(msgBytes));
                    break;
                case LOGIN:
                    returnPacket.addMessage(LoginMsg.decode(msgBytes));
                    break;
                case LOGOUT:
                    returnPacket.addMessage(LogoutMsg.decode(msgBytes));
                    break;
                case CONTROLS:
                    break;
                case GAME_STATE:
                    break;
                case CHAT_MESSAGE:
                    break;
            }
        }

        return returnPacket;
    }
}
