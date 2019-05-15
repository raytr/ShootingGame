package game.shared.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class PacketWriter {
    //Will create messages given a max packet size
    //Tries to separate unreliable and reliable messages
    static List<Packet> write(Set<Integer> seqNums, NetManager nh,
                              int maxBytesPerPacket, List<Message> msgList) {
        List<Message> reliableList = new ArrayList<Message>();
        List<Message> unreliableList = new ArrayList<Message>();
        List<Packet> returnList = new ArrayList<Packet>();
        for (Message m : msgList) {
            if (m.isReliable()) reliableList.add(m);
            else unreliableList.add(m);
        }



        //System.out.println("Sending " + reliableList.size() + " reliable messages");
        //System.out.println("Sending  " + unreliableList.size() + " unreliable messages");
        makePackets(seqNums, true, nh, maxBytesPerPacket, reliableList, returnList);
        makePackets(seqNums, false, nh, maxBytesPerPacket, unreliableList, returnList);

        //System.out.println("Made " + returnList.size() + " packets");

        return returnList;
    }

    private static void makePackets(Set<Integer> seqNums, boolean isReliable, NetManager nh,
                                    int maxBytesPerPacket, List<Message> msgList, List<Packet> addedList) {
        int seqNum = nh.getNextSeqNum();
        Packet currentPacket = new Packet(seqNum, isReliable);
        seqNums.add(seqNum);

        for (int i = 0; i < msgList.size(); i++) {
            Message m = msgList.get(i);
            if (currentPacket.getSize() + m.getSize() < maxBytesPerPacket) {
                currentPacket.addMessage(m);
                if (i == msgList.size() - 1) {
                    addedList.add(currentPacket);
                    return;
                }
            } else {
                addedList.add(currentPacket);
                seqNum = nh.getNextSeqNum();
                currentPacket = new Packet(seqNum, isReliable);
                seqNums.add(seqNum);
                currentPacket.addMessage(m);
                if (i == msgList.size() - 1) {
                    addedList.add(currentPacket);
                    return;
                }
            }
        }
    }
}
