package game.shared.net;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Objects;

//Object used as a key in maps
public class InetAddrSeqNumKey {
    private final InetSocketAddress addr;
    private final int seqNum;

    public InetAddrSeqNumKey(InetSocketAddress addr, int seqNum) {
        this.addr = addr;
        this.seqNum = seqNum;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other instanceof InetAddrSeqNumKey) {
            InetAddrSeqNumKey that = (InetAddrSeqNumKey) other;
            return Objects.equals(this.addr, that.getAddr())
                    && Objects.equals(this.seqNum, that.getSeqNum());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addr, seqNum);
    }

    public static void main(String[] args) {
        HashMap<InetAddrSeqNumKey, Integer> testMap = new HashMap<>();
        testMap.put(new InetAddrSeqNumKey(new InetSocketAddress("localhost", 4444), 5), 5);
        testMap.put(new InetAddrSeqNumKey(new InetSocketAddress("localhost", 4444), 5), 5);
        System.out.println(testMap.get(new InetAddrSeqNumKey(new InetSocketAddress("localhost", 4444), 5)));
    }

}
