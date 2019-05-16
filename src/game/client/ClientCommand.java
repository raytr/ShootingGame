package game.client;

import game.shared.net.messages.Command;

public interface ClientCommand {
    void execute(Sprite s);

}
