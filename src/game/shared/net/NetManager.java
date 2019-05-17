package game.shared.net;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetManager {
    private int seqNum = (int) (Math.random() * ((999) + 1));
    private final UdpSender udpSender;
    private final UdpReceiver udpReceiver;
    private final ReceivePacketHandler receivePacketHandler;
    private DatagramSocket socket;

    private final Set<InetSocketAddress> sendClients = Collections.synchronizedSet(new HashSet<>());
    private List<Message> msgList;

    //Start as a server
    public NetManager(ReceivePacketHandler receivePacketHandler, int port) {
        this.receivePacketHandler = receivePacketHandler;
        try {
            socket = new DatagramSocket(port);
            System.out.println("Server socket started on port: " + socket.getLocalPort());
        } catch (java.io.IOException e) {
            System.err.println("Could not create datagram socket.");
        }
        udpReceiver = new UdpReceiver(this, socket);
        udpSender = new UdpSender(this, socket);
        udpSender.start();
        udpReceiver.start();
    }

    //Start as a client
    public NetManager(ReceivePacketHandler receivePacketHandler, InetSocketAddress serverAddress) {
        this.receivePacketHandler = receivePacketHandler;
        try {
            socket = new DatagramSocket();
            System.out.println("Client Socket created on port: " + socket.getLocalPort());
        } catch (java.io.IOException e) {
            System.err.println("Could not create datagram socket.");
        }
        udpSender = new UdpSender(this, socket);
        udpReceiver = new UdpReceiver(this, socket);
        udpSender.start();
        udpReceiver.start();
        addSendClient(serverAddress);
    }

    public void stop() {
        udpSender.stopRunning();
        udpSender.interrupt();
        udpReceiver.interrupt();
        socket.close();
        System.out.println("Nethandler stopped");
    }

    public void addSendClient(InetSocketAddress i) {
        sendClients.add(i);
        //System.out.println("ADDED NEW SEND CLIENT CURRENT #" + sendClients.size());
    }

    public void removeSendClient(InetSocketAddress i) {
        sendClients.remove(i);
    }

    public int sendMessage(Message m) {
        return this.sMessage(m, null, 0, 0, null, null);
    }

    //Returns the seq num
    public int sendMessage(Message m, InetSocketAddress addr) {
        return this.sMessage(m, addr, 0, 0, null, null);
    }

    public int sendMessage(Message m, int maxAcks, int ackDelay) {
        return this.sMessage(m, null, maxAcks, ackDelay, null, null);
    }

    public int sendMessage(Message m, InetSocketAddress addr, int maxAcks, int ackDelay) {
        return this.sMessage(m, addr, maxAcks, ackDelay, null, null);
    }

    public int sendMessage(Message m, PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        return this.sMessage(m, null, 0, 0, onSuccess, onFailure);
    }

    public int sendMessage(Message m, InetSocketAddress addr,
                           PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        return this.sMessage(m, addr, 0, 0, onSuccess, onFailure);
    }

    public int sendMessage(Message m, InetSocketAddress addr, int maxAcks, int ackDelay,
                           PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        return this.sMessage(m, addr, maxAcks, ackDelay, onSuccess, onFailure);
    }

    //Send messages in packet
    //If there are too many, then divides into multiple messages automatically
    public Set<Integer> sendMessages(List<Message> msgList) {
        //System.out.println("Attempting to write " + msgList.size() + " messages.");
        Set<Integer> seqNums = new HashSet<Integer>();
        List<Packet> packetList = PacketWriter.write(seqNums, this, 1024, msgList);
        for (Packet p : packetList) {
            //System.out.println("Packet size " + p.getSize());
            udpSender.queuePacket(p);
        }
        return seqNums;
    }

    //Send messages in packet
    //If there are too many, then divides into multiple messages automatically
    public Set<Integer> sendMessages(List<Message> msgList,InetSocketAddress addr) {
        //System.out.println("Attempting to write " + msgList.size() + " messages.");
        Set<Integer> seqNums = new HashSet<Integer>();
        List<Packet> packetList = PacketWriter.write(seqNums, this, 1024, msgList);
        for (Packet p : packetList) {
            p.setSocketAddress(addr);
            //System.out.println("Packet size " + p.getSize());
            udpSender.queuePacket(p);
        }
        return seqNums;
    }

    int getNextSeqNum() {
        seqNum++;
        return seqNum;
    }

    Set<InetSocketAddress> getSendClients() {
        return sendClients;
    }

    ReceivePacketHandler getReceivePacketHandler() {
        return receivePacketHandler;
    }

    void removeWaitingAck(InetSocketAddress addr, int seqNum) {
        udpSender.removeWaitingAck(addr, seqNum);
    }


    private int sMessage(Message m, InetSocketAddress addr, int maxAcks,
                         int ackDelayMS, PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        boolean isReliable = m.isReliable();

        //Force true if any of these parameters were specifically set
        if (maxAcks > 0 || ackDelayMS > 0
                || onSuccess != null || onFailure != null) isReliable = true;

        int seqNum = this.getNextSeqNum();
        Packet p = new Packet(seqNum, isReliable);
        p.addMessage(m);
        if (maxAcks != 0) p.setMaxAcks(maxAcks);
        if (ackDelayMS != 0) p.setAckDelayMS(ackDelayMS);
        p.setSocketAddress(addr);
        p.setOnSuccess(onSuccess);
        p.setOnFailure(onFailure);
        udpSender.queuePacket(p);
        return seqNum;

    }
}
