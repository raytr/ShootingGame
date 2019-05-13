package game.shared.net.packets;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class LoginMsg implements Message {
    private final int playerNum;
    private final byte[] bytes;
    private final String name;

    private LoginMsg(byte[] bytes, String name, int playerNum) {
        this.name = name;
        this.bytes = bytes;
        this.playerNum = playerNum;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static LoginMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int playerNum = b.getInt();
        String name = StandardCharsets.UTF_8.decode(b).toString();
        return new LoginMsg(bytes, name, playerNum);
    }

    public static LoginMsg encode(String name, int playerNum) {
        byte[] stringBytes = name.getBytes();
        //+1 int for the messageType
        //+1 int for the size of data
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4 + stringBytes.length);
        //Add message type
        b.putInt(MsgType.LOGIN.getValue());
        //Add message size
        b.putInt(4 + stringBytes.length);
        //Put in content
        b.putInt(playerNum);
        b.put(stringBytes);
        return new LoginMsg(b.array(), name, playerNum);
    }

    public String getName() {
        return name;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.LOGIN;
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
