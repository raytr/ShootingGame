package game.shared.net.messages;


import game.shared.EntityType;
import game.shared.net.Message;

import java.nio.ByteBuffer;

public class EntityDeleteMsg implements Message {
    private final byte[] bytes;
    private final int id;

    private EntityDeleteMsg(byte[] bytes,int id){
        this.bytes = bytes;
        this.id = id;
    }


    //Expecting CONTENT only (without messageType + msgSize
    public static EntityDeleteMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int id = b.getInt();
        return new EntityDeleteMsg(bytes, id);
    }

    public static EntityDeleteMsg encode(int id){
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4);
        //Add message type
        b.putInt(MsgType.ENTITY_DELETE.getValue());
        //Add message size
        b.putInt(4);
        //Put in content
        b.putInt(id);
        return new EntityDeleteMsg(b.array(), id);
    }

    public int getId() {
        return id;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.ENTITY_DELETE;
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
