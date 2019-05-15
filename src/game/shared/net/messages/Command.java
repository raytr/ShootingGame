package game.shared.net.messages;


import game.shared.net.messages.commands.CommandType;

public interface Command {
    int getByteSize();

    byte[] getBytes();

    CommandType getType();
    boolean isReliable();
}
