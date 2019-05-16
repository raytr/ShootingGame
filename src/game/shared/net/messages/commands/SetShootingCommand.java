package game.shared.net.messages.commands;

import game.shared.net.messages.Command;

public class SetShootingCommand implements Command {
    final byte[] bytes;
    private final boolean isShooting;
    private SetShootingCommand(byte[] bytes,boolean isShooting){
        this.bytes = bytes;
        this.isShooting = isShooting;
    }
    //Expecting CONTENT only (without messageType + msgSize
    public static SetShootingCommand decode(byte[] bytes) {
        boolean isShooting = (bytes[0] == 1);
        return new SetShootingCommand(bytes,isShooting);
    }

    public static SetShootingCommand encode(boolean isShooting) {
        return new SetShootingCommand(new byte[]{isShooting ? (byte)1 : (byte)0},isShooting);
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
        return CommandType.SET_SHOOTING;
    }

    @Override
    public boolean isReliable() {
        return true;
    }

    public boolean getIsShooting() {
        return isShooting;
    }
}
