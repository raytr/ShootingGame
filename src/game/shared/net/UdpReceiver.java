package game.shared.net;

import game.shared.net.messages.AckMsg;
import game.shared.net.messages.MsgType;

import java.net.*;

class UdpReceiver extends Thread {
    private final byte[] buffer = new byte[1024];
    private final DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
    private final NetManager nh;
    private final DatagramSocket socket;

    UdpReceiver(NetManager nh, DatagramSocket socket) {
        this.nh = nh;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                socket.receive(incoming);
                InetAddress address = incoming.getAddress();
                int port = incoming.getPort();
                Packet packet = PacketReader.read(incoming);
                for (Message m : packet.getMsgList()) {
                    if (m.getMsgType().equals(MsgType.ACK)) {
                        nh.removeWaitingAck(packet.getSocketAddress(),
                                ((AckMsg) m).getAckedSeqNum());
                    }
                }
                //Send an ack back!!
                if (packet.isReliable()) {
                    //System.out.println("Sending ACK! for packet " + packet.getSeqNum());
                    nh.sendMessage(AckMsg.encode(packet), new InetSocketAddress(incoming.getAddress(), incoming.getPort()));
                }
                nh.getReceivePacketHandler().handleReceive(packet);
                incoming.setLength(buffer.length);
            }
        } catch (SocketException e) {
            //Do nothing
            System.out.println(e);
        } catch (Exception e) {
            System.err.println("Server  " + e);
            e.printStackTrace();
            socket.close();
        }

    }
}
