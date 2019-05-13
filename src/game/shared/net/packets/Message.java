package game.shared.net.packets;

public interface Message {
    int HEADER_BYTE_SIZE = 8;

    MsgType getMsgType();

    byte[] getBytes();

    int getSize();

    boolean isReliable();
}
