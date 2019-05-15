package game.shared.net.messages;

import game.shared.EntityType;
import game.shared.net.Message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class EntityCreateMsg implements Message {
    private final String name ;
    private final byte[] bytes;
    private final EntityType entityType;
    private final int id;
    final float x0;
    final float y0;

    private EntityCreateMsg(byte[] bytes,int id, EntityType type, float x0, float y0,String name) {
        this.bytes = bytes;
        this.id = id;
        entityType = type;
        this.x0 = x0;
        this.y0 = y0;
        this.name = name;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static EntityCreateMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int id = b.getInt();
        EntityType entityType = EntityType.valueOf(b.getInt());
        float x0 = b.getFloat();
        float y0 = b.getFloat();

        String name = StandardCharsets.UTF_8.decode(b).toString();
        return new EntityCreateMsg(bytes, id, entityType,x0,y0,name);
    }

    public static EntityCreateMsg encode(int id,EntityType type,double x0,double y0,String name) {
        byte[] stringBytes = name.getBytes();
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4 +4 + 4+4+stringBytes.length);
        //Add message type
        b.putInt(MsgType.ENTITY_CREATE.getValue());
        //Add message size
        b.putInt(4 + 4 + 4 + 4 + stringBytes.length);
        //Put in content
        b.putInt(id);
        b.putInt(type.getValue());
        b.putFloat((float)x0);
        b.putFloat((float)y0);
        b.put(stringBytes);
        return new EntityCreateMsg(b.array(), id, type,(float)x0,(float)y0,name);
    }

    public String getName(){return name;}
    public EntityType getEntityType() {
        return entityType;
    }

    public int getId() {
        return id;
    }

    public float getX0() {
        return x0;
    }

    public float getY0() {
        return y0;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.ENTITY_CREATE;
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
