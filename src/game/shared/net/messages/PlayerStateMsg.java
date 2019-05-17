package game.shared.net.messages;

import game.shared.net.Message;

import java.nio.ByteBuffer;

public class PlayerStateMsg implements Message {
    private final int playerNum;
    private final byte[] bytes;
    private final int score;


    private PlayerStateMsg(byte[] bytes, int playerNum,int score) {
        this.bytes = bytes;
        this.playerNum = playerNum;
        this.score = score;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static PlayerStateMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int playerNum = b.getInt();
        int score = b.getInt();
        return new PlayerStateMsg(bytes, playerNum,score);
    }

    public static PlayerStateMsg encode(int playerNum,int score) {
        //+1 int for the messageType
        //+1 int for the size of data
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4 + 4);
        //Add message type
        b.putInt(MsgType.PLAYER_STATE.getValue());
        //Add message size
        b.putInt(4+4);
        //Put in content
        b.putInt(playerNum);
        b.putInt(score);
        return new PlayerStateMsg(b.array(), playerNum,score);
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int getScore() {
        return score;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.PLAYER_STATE;
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
