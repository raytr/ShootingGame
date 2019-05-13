package game.shared.net;

import game.shared.net.packets.Packet;

public interface ReceivePacketHandler {
    void handleReceive(Packet p);
}
