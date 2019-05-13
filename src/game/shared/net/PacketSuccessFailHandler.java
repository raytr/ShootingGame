package game.shared.net;

import java.net.InetSocketAddress;

public interface PacketSuccessFailHandler {
    //The address that the packet was sent to
    void run(InetSocketAddress addr);
}
