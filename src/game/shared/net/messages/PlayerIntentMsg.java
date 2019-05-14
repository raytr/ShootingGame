package game.shared.net.messages;

import game.shared.net.Message;

import java.nio.ByteBuffer;

//What the player is trying to do
public class PlayerIntentMsg implements Message {
    private final byte[] bytes;
    private final int playerNum;
    private PlayerIntentMsg(byte[] bytes, int playerNum) {
        this.playerNum = playerNum;
        this.bytes = bytes;
    }

    //Expecting CONTENT only (without messageType + msgSize
    public static PlayerIntentMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int playerNum = b.getInt();
        short moveX = b.getShort();
        short moveY = b.getShort();
        float shootAngle = b.getFloat();
        boolean isShooting = b.get() == (byte) 1;
        return new PlayerIntentMsg(bytes, playerNum);
    }

    public static PlayerIntentMsg encode(int playerNum,short moveX,short moveY,float shootAngle,boolean isShooting) {
        //+1 int for the messageType
        //+1 int for the size of data
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4);
        //Add message type
        b.putInt(MsgType.PLAYER_INTENT.getValue());
        //Add message size
        b.putInt(13);
        //Put in content
        b.putInt(playerNum);
        b.putShort(moveX);
        b.putShort(moveY);
        b.putFloat(shootAngle);
        b.put(isShooting ? (byte) 1 : (byte)0);
        return new PlayerIntentMsg(b.array(), playerNum);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.PLAYER_INTENT;
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
        return false;
    }
}
