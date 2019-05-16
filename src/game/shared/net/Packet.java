package game.shared.net;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Packet {
    public static final int HEADER_BYTE_SIZE = 5;
    //Used if isReliable = true
    private int numAcks = 0;
    private int maxAcks = 5;
    private int ackDelayMS = 100;
    //CONTAINED IN PACKET HEADER
    private final boolean isReliable;
    private final int seqNum;
    private final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    private PacketSuccessFailHandler onSuccess;
    private PacketSuccessFailHandler onFailure;

    private final List<Message> msgList;
    private InetSocketAddress address;

    public Packet(int seqNum, boolean isReliable) {
        msgList = new ArrayList<Message>();
        this.seqNum = seqNum;
        this.isReliable = isReliable;
        try {
            //Write header
            byteOut.write(ByteBuffer.allocate(HEADER_BYTE_SIZE).putInt(seqNum).put(isReliable ? (byte) 1 : (byte) 0).array());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMessage(Message m) {

        try {
            if (m.isReliable() && !isReliable) {
                throw new Exception("Tried putting a reliable message into an unreliable packet");
            }
            msgList.add(m);
            byteOut.write(m.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMsgList() {
        return msgList;
    }

    public InetSocketAddress getSocketAddress() {
        return address;
    }

    public void setSocketAddress(InetSocketAddress addr) {
        address = addr;
    }

    public byte[] getBytes() {
        return byteOut.toByteArray();
    }

    public int getSize() {
        return byteOut.size();
    }

    public boolean isReliable() {
        return isReliable;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public int getMaxAcks() {
        return maxAcks;
    }

    public int getAckDelayMS() {
        return ackDelayMS;
    }

    public void setMaxAcks(int s) {
        maxAcks = s;
    }

    public void setAckDelayMS(int s) {
        ackDelayMS = s;
    }

    public void setOnSuccess(PacketSuccessFailHandler r) {
        this.onSuccess = r;
    }

    public void setOnFailure(PacketSuccessFailHandler r) {
        this.onFailure = r;
    }

    public void onSuccess(InetSocketAddress addr) {
        if (this.onSuccess != null) onSuccess.run(addr);
    }

    public void onFailure(InetSocketAddress addr) {
        if (this.onFailure != null) onFailure.run(addr);
    }

    public int getNumAcks() {
        return numAcks;
    }

    public void incrementAcks() {
        numAcks++;
    }
}
