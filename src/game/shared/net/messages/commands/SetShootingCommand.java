package game.shared.net.messages.commands;

import game.shared.net.messages.Command;

public class StartShootCommand implements Command {
    private StartShootCommand(){
    }
    //Expecting CONTENT only (without messageType + msgSize
    public static StartShootCommand decode(byte[] bytes) {
        return new StartShootCommand();
    }

    public static StartShootCommand encode() {
        return new StartShootCommand();
    }
    @Override
    public int getByteSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public CommandType getType() {
        return CommandType.START_SHOOT;
    }

    @Override
    public boolean isReliable() {
        return true;
    }
}
