package game.client.commands;

import game.client.ClientCommand;
import game.client.ClientServerCommunicationCommand;
import game.client.Sprite;
import game.shared.net.messages.Command;
import game.shared.net.messages.commands.SetShootingCommand;

public class ClientSetShootingCommand implements ClientCommand, ClientServerCommunicationCommand {
    private SetShootingCommand setShootingCommand;
    public ClientSetShootingCommand(SetShootingCommand ssc){setShootingCommand = ssc;
    }
    @Override
    public void execute(Sprite s) {

    }

    @Override
    public Command getCommand() {
        return setShootingCommand;
    }
}
