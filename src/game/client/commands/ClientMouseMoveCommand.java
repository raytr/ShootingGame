package game.client.commands;

import game.client.ClientCommand;
import game.client.ClientServerCommunicationCommand;
import game.client.Sprite;
import game.shared.net.messages.Command;
import game.shared.net.messages.commands.MouseMoveCommand;

public class ClientMouseMoveCommand implements ClientCommand, ClientServerCommunicationCommand {
    MouseMoveCommand mmc;
    public ClientMouseMoveCommand(MouseMoveCommand mmc){
        this.mmc = mmc;

    }
    @Override
    public void execute(Sprite s) {
        s.setRotation(mmc.getAngle());
    }
    @Override
    public Command getCommand() {
        return mmc;
    }
}
