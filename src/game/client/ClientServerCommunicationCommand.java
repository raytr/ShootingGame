package game.client;

import game.shared.net.messages.Command;

public interface ClientServerCommunicationCommand {
    Command getCommand();
}
