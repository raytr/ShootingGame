package game.shared.net;

import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

class UdpSender extends Thread {
    final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    private volatile boolean stillRunning = true;
    //Ids of the messages that we are waiting on acks for
    //<id, Packet>
    private final Map<InetAddrSeqNumKey, Packet> awaitingAckIDs =
            Collections.synchronizedMap(new HashMap<InetAddrSeqNumKey, Packet>());

    private final BlockingQueue<Packet> queuedPackets = new LinkedBlockingQueue<>();
    private final DatagramSocket socket;
    private final NetManager nh;

    UdpSender(NetManager nh, DatagramSocket socket) {
        this.nh = nh;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (stillRunning) {
                //Get info from packet in packet queue
                Packet packet = queuedPackets.take();

                //Create a DatagramPacket
                byte[] bytes = packet.getBytes();
                DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
                dp.setData(bytes);
                //System.out.println("ORIGIN BYTE LENGTH" + bytes.length);
                dp.setLength(bytes.length);

                //Set the address & send
                InetSocketAddress socketAddr = packet.getSocketAddress();
                if (socketAddr != null) {
                    //System.out.println("PERSONAL MSG" + socketAddr);
                    int port = socketAddr.getPort();
                    InetAddress address = socketAddr.getAddress();
                    dp.setPort(port);
                    dp.setAddress(address);
                    socket.send(dp);
                } else {
                    //Null socketAddress; send to all clients
                    Set<InetSocketAddress> clientList = nh.getSendClients();
                    synchronized (clientList) {
                        for (InetSocketAddress isa : clientList) {
                            dp.setPort(isa.getPort());
                            dp.setAddress(isa.getAddress());
                            socket.send(dp);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            //Do nothing
        } catch (SocketException e) {
            //Do nothing
            System.out.println(e);
        } catch (Exception e) {
            System.err.println("Client UdpSender  " + e);
            e.printStackTrace();
        }

    }

    public void stopRunning() {
        stillRunning = false;
        exec.shutdown();
    }

    public void queuePacket(Packet p) {
        if (p.isReliable()) {
            InetAddrSeqNumKey key;
            if (p.getSocketAddress() != null) {
                key = new InetAddrSeqNumKey(p.getSocketAddress(), p.getSeqNum());
                //System.out.println("DIRECTED SCHEDULE RECHECK FOR " + p.getSocketAddress() + " " + p);
                scheduleRechecks(key, p);
            } else {
                for (InetSocketAddress s : nh.getSendClients()) {
                    //System.out.println("SCHEDULE RECHECK FOR " + s + " " + p);
                    key = new InetAddrSeqNumKey(s, p.getSeqNum());
                    scheduleRechecks(key, p);
                }
            }
        }
        queuedPackets.offer(p);
    }

    public void removeWaitingAck(InetSocketAddress addr, int seqNum) {
        try {
            InetAddrSeqNumKey key = new InetAddrSeqNumKey(addr, seqNum);
            awaitingAckIDs.get(key).onSuccess(addr);
            awaitingAckIDs.remove(key);
        }catch (NullPointerException np){
            np.printStackTrace();
        }
    }

    private void scheduleRechecks(InetAddrSeqNumKey key, Packet p) {
        if (!awaitingAckIDs.containsKey(key)) awaitingAckIDs.put(key, p);

        exec.schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (awaitingAckIDs) {
                    int numAcks = (awaitingAckIDs.get(key) == null ? -1 : awaitingAckIDs.get(key).getNumAcks());
                    //System.out.println("NUM ACKS: " +numAcks);
                    if (awaitingAckIDs.containsKey(key)) {
                        if (numAcks < p.getMaxAcks()) {
                            awaitingAckIDs.get(key).incrementAcks();
                            queuePacket(p);
                        } else {
                            awaitingAckIDs.remove(key);
                            p.onFailure(key.getAddr());
                        }
                    }
                }
            }
        }, p.getAckDelayMS(), TimeUnit.MILLISECONDS);
    }

}
