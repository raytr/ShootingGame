package game.shared.net;

import game.shared.net.messages.*;

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

            returnPacket.addMessage(msgType.decode(msgBytes));
        }

        return returnPacket;
    }
}
