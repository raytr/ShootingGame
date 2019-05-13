package game.shared.net.packets;

import java.nio.ByteBuffer;

public class AckMsg implements Message {
    private final byte[] bytes;
    final int ackedSeqNum;

    private AckMsg(byte[] bytes, int ackdSeqNum) {
        this.ackedSeqNum = ackdSeqNum;
        this.bytes = bytes;
    }

    //Expecting CONTENT only (without messageType + msgSize
    public static AckMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        return new AckMsg(bytes, b.getInt());
    }

    public static AckMsg encode(Packet p) {
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4);
        //Add message type
        b.putInt(MsgType.ACK.getValue());
        //Add message size
        b.putInt(4);
        //Put in content
        b.putInt(p.getSeqNum());
        return new AckMsg(b.array(), p.getSeqNum());
    }

    public int getAckedSeqNum() {
        return ackedSeqNum;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.ACK;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public int getSize() {
        return bytes.length;
    }

    @Override
    public boolean isReliable() {
        return false;
    }
}
