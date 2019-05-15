package game.shared.net.messages;

import game.shared.net.Message;
import game.shared.net.messages.commands.CommandType;

import java.nio.ByteBuffer;

//<PLAYER NUM><COMMAND SIZE><COMMAND TYPE><COMMAND PAYLOAD>
public class CommandMsg implements Message{
    private final int playerNum;
    private final byte[] bytes;
    private final Command command;
    private final boolean isReliable;

    private CommandMsg(byte[] bytes,int playerNum, Command command,boolean isReliable) {
        this.command = command;
        this.bytes = bytes;
        this.playerNum = playerNum;
        this.isReliable = isReliable;
    }



    //Expecting CONTENT only (without messageType + msgSize
    public static CommandMsg decode(byte[] bytes) {
        ByteBuffer b = ByteBuffer.wrap(bytes);
        //Read the playerNum
        int playerNum = b.getInt();
        //Read the command size
        int size = b.getInt();
        //Read the command type
        CommandType commandType = CommandType.valueOf(b.getInt());
        byte[] commandBytes = new byte[size];
        b.get(commandBytes,0,size);
        Command command = commandType.decode(commandBytes);
        return new CommandMsg(bytes, playerNum, command,command.isReliable());
    }

    public static CommandMsg encode(int playerNum, Command command) {
        //+1 int for the messageType
        //+1 int for the size of data
        ByteBuffer b = ByteBuffer.allocate(Message.HEADER_BYTE_SIZE + 4+4 +4 + command.getByteSize());
        //Add message type
        b.putInt(MsgType.PLAYER_COMMAND.getValue());
        //Add message size
        b.putInt(4 + 4 +4 + command.getByteSize());

        //Add content
        b.putInt(playerNum);
        b.putInt(command.getByteSize());
        b.putInt(command.getType().getValue()); // + 4 size
        b.put(command.getBytes()); //+ however big this is

        return new CommandMsg(b.array(), playerNum, command,command.isReliable());
    }
    public Command getCommand(){return command;}
    public int getPlayerNum(){return playerNum;}

    @Override
    public MsgType getMsgType() {
        return MsgType.PLAYER_COMMAND;
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
        return isReliable;
    }
}
