package game.shared.net.messages.commands;

import game.shared.net.messages.Command;

import java.nio.ByteBuffer;

public class ChangeAngleCommand implements Command {
    private byte[] bytes;
    private float angle;

    private ChangeAngleCommand(byte[] bytes, float angle){
        this.bytes = bytes;
        this.angle = angle;
    }
    //Expecting CONTENT only (without messageType + msgSize
    public static ChangeAngleCommand decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        float angle = b.getFloat();
        return new ChangeAngleCommand(bytes, angle);
    }

    public static ChangeAngleCommand encode(float angle) {
        ByteBuffer b = ByteBuffer.allocate(8);
        b.putFloat(angle);
        return new ChangeAngleCommand(b.array(), angle);
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
        return CommandType.CHANGE_ANGLE;
    }

    @Override
    public boolean isReliable() {
        return false;
    }

    public float getAngle() {
        return angle;
    }
}

