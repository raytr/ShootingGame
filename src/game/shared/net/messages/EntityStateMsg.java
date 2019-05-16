package game.shared.net.messages;

import game.shared.net.Message;

import java.nio.ByteBuffer;

public class EntityStateMsg implements Message {
    private final byte[] bytes;
    private final int id;
    private final float x;
    private final float y;
    private final float angle;
    private final int hp;

    private EntityStateMsg(byte[] bytes,int id, int hp, float x0, float y0,float angle) {
        this.angle = angle;
        this.bytes = bytes;
        this.id = id;
        this.hp = hp;
        this.x = x0;
        this.y = y0;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static EntityStateMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int id = b.getInt();
        int hp = b.getInt();
        float x0 = b.getFloat();
        float y0 = b.getFloat();
        float angle = b.getFloat();
        return new EntityStateMsg(bytes, id,hp,x0,y0,angle);
    }

    public static EntityStateMsg encode(int id,int hp,double x,double y,double angle) {
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4 +4 + 4 + 4 + 4);
        //Add message type
        b.putInt(MsgType.ENTITY_STATE.getValue());
        //Add message size
        b.putInt(4 + 4 + 4 + 4 + 4);
        //Put in content
        b.putInt(id);
        b.putInt(hp);
        b.putFloat((float)x);
        b.putFloat((float)y);
        b.putFloat((float)angle);
        return new EntityStateMsg(b.array(), id, hp,(float)x,(float)y,(float)angle);
    }
    public int getHp(){return hp;}

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public float getAngle(){
        return angle;
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
