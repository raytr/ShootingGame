package game.shared.net.messages.commands;

import com.sun.org.apache.xpath.internal.operations.Bool;
import game.shared.net.messages.Command;

import java.nio.ByteBuffer;

public class MoveCommand implements Command {
    private byte[] bytes;
    private Boolean moveUp;
    private Boolean moveDown;
    private Boolean moveRight;
    private Boolean moveLeft;


    private MoveCommand(byte[] bytes, Boolean moveUp, Boolean moveDown, Boolean moveLeft, Boolean moveRight){
        this.bytes = bytes;
        this.moveDown =moveDown;
        this.moveRight = moveRight;
        this.moveLeft = moveLeft;
        this.moveUp = moveUp;
    }
    //Expecting CONTENT only (without messageType + msgSize
    public static MoveCommand decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        Boolean moveUp = short2Bool(b.getShort());
        Boolean moveDown = short2Bool(b.getShort());
        Boolean moveLeft = short2Bool(b.getShort());
        Boolean moveRight = short2Bool(b.getShort());
        return new MoveCommand(bytes, moveUp,moveDown,moveLeft,moveRight);
    }

    public static MoveCommand encode(Boolean moveUp,Boolean moveDown, Boolean moveLeft, Boolean moveRight) {
        ByteBuffer b = ByteBuffer.allocate(8);
        b.putShort(bool2Short(moveUp));
        b.putShort(bool2Short(moveDown));
        b.putShort(bool2Short(moveLeft));
        b.putShort(bool2Short(moveRight));
        return new MoveCommand(b.array(), moveUp, moveDown,moveLeft,moveRight);
    }
    private static short bool2Short(Boolean b){
        return b == null ? 2 : (b ? (short)1 : (short)0);
    }
    private static Boolean short2Bool(short s){
        return s == 2 ? null : (s == 1);
    }

    public Boolean getMoveUp() {
        return moveUp;
    }

    public Boolean getMoveDown() {
        return moveDown;
    }

    public Boolean getMoveRight() {
        return moveRight;
    }

    public Boolean getMoveLeft() {
        return moveLeft;
    }
    @Override
    public int getByteSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public CommandType getType() {
        return CommandType.MOVE;
    }

    @Override
    public boolean isReliable() {
        return true;
    }
}
