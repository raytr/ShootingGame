package game.shared.net.messages.commands;

import game.shared.net.messages.Command;

import java.nio.ByteBuffer;

public class MouseMoveCommand implements Command {
    private byte[] bytes;
    private float angle;

    private MouseMoveCommand(byte[] bytes, float angle){
        this.bytes = bytes;
        this.angle = angle;
    }
    //Expecting CONTENT only (without messageType + msgSize
    public static MouseMoveCommand decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        float angle = b.getFloat();
        return new MouseMoveCommand(bytes, angle);
    }

    public static MouseMoveCommand encode(float angle) {
        ByteBuffer b = ByteBuffer.allocate(8);
        b.putFloat(angle);
        return new MouseMoveCommand(b.array(), angle);
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
        return CommandType.MOUSE_MOVE;
    }

    @Override
    public boolean isReliable() {
        return false;
    }

    public float getAngle() {
        return angle;
    }
}

