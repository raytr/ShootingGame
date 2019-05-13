package game.shared.net.packets;

import java.nio.ByteBuffer;

public class LogoutMsg implements Message {
    private final byte[] bytes;
    private final int playerNum;

    private LogoutMsg(byte[] bytes, int playerNum) {
        this.playerNum = playerNum;
        this.bytes = bytes;
    }

    //Expecting CONTENT only (without messageType + msgSize
    public static LogoutMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int playerNum = b.getInt();
        return new LogoutMsg(bytes, playerNum);
    }

    public static LogoutMsg encode(int playerNum) {
        //+1 int for the messageType
        //+1 int for the size of data
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4);
        //Add message type
        b.putInt(MsgType.LOGOUT.getValue());
        //Add message size
        b.putInt(4);
        //Put in content
        b.putInt(playerNum);
        return new LogoutMsg(b.array(), playerNum);
    }

    public int getPlayerNum() {
        return playerNum;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.LOGOUT;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public int getSize() {
        return bytes.length;
    }

    @Override
    public boolean isReliable() {
        return true;
    }
}
