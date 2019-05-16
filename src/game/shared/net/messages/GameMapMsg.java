package game.shared.net.messages;

import game.shared.net.Message;

import java.nio.ByteBuffer;

public class GameMapMsg implements Message {
    private final byte[] bytes;
    private final float width;
    private final float height;

    private GameMapMsg(byte[] bytes,float width, float height){
        this.bytes = bytes;
        this.width = width;
        this.height = height;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static GameMapMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        float width = b.getFloat();
        float height = b.getFloat();
        return new GameMapMsg(bytes, width,height);
    }

    public static GameMapMsg encode(double width, double height){
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4+4);
        //Add message type
        b.putInt(MsgType.GAME_MAP.getValue());
        //Add message size
        b.putInt(4+4);
        //Put in content
        b.putFloat((float)width);
        b.putFloat((float)height);
        return new GameMapMsg(b.array(), (float)width,(float)height);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.GAME_MAP;
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
