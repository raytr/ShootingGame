package game.shared.net;

import game.shared.net.messages.MsgType;

public interface Message {
    int HEADER_BYTE_SIZE = 8;

    MsgType getMsgType();

    byte[] getBytes();

    int getSize();

    boolean isReliable();
}
