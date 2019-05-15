package game.shared.net.messages;

import game.shared.net.Message;

import java.nio.ByteBuffer;

public class EntityStateMsg implements Message {
    private final byte[] bytes;
    private final int id;
    final float x;
    final float y;

    private EntityStateMsg(byte[] bytes,int id, float x0, float y0) {
        this.bytes = bytes;
        this.id = id;
        this.x = x0;
        this.y = y0;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static EntityStateMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int id = b.getInt();
        float x0 = b.getFloat();
        float y0 = b.getFloat();
        return new EntityStateMsg(bytes, id,x0,y0);
    }

    public static EntityStateMsg encode(int id,double x,double y) {
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4 +4 + 4);
        //Add message type
        b.putInt(MsgType.ENTITY_STATE.getValue());
        //Add message size
        b.putInt(4 + 4 + 4);
        //Put in content
        b.putInt(id);
        b.putFloat((float)x);
        b.putFloat((float)y);
        return new EntityStateMsg(b.array(), id, (float)x,(float)y);
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.ENTITY_STATE;
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
