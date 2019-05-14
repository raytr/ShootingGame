package game.shared.net.messages;

import game.shared.net.Message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ChatMsg implements Message {
    private final int playerNum;
    private final String message;
    private final byte[] bytes;


    private ChatMsg(byte[] bytes, String message, int playerNum) {
        this.message = message;
        this.bytes = bytes;
        this.playerNum = playerNum;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static ChatMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int playerNum = b.getInt();
        String message = StandardCharsets.UTF_8.decode(b).toString();
        return new ChatMsg(bytes, message, playerNum);
    }

    public static ChatMsg encode(String msg, int playerNum) {
        byte[] stringBytes = msg.getBytes();
        //+1 int for the messageType
        //+1 int for the size of data
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4 + stringBytes.length);
        //Add message type
        b.putInt(MsgType.CHAT_MESSAGE.getValue());
        //Add message size
        b.putInt(4 + stringBytes.length);
        //Put in content
        b.putInt(playerNum);
        b.put(stringBytes);
        return new ChatMsg(b.array(), msg, playerNum);
    }
    @Override
    public MsgType getMsgType() {
        return MsgType.CHAT_MESSAGE;
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

    public int getPlayerNum() {
        return playerNum;
    }

    public String getMsg() {
        return message;
    }
}
