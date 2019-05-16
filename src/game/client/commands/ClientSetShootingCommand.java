package game.client.commands;

import game.client.ClientCommand;
import game.client.ClientServerCommunicationCommand;
import game.client.Sprite;
import game.shared.net.messages.Command;
import game.shared.net.messages.commands.StartShootCommand;

public class ClientStartShootCommand implements ClientCommand, ClientServerCommunicationCommand {
    private StartShootCommand startShootCommand;
    public ClientStartShootCommand(StartShootCommand ssc){
        startShootCommand = ssc;
    }
    @Override
    public void execute(Sprite s) {
    }

    @Override
    public Command getCommand() {
        return startShootCommand;
    }
}
