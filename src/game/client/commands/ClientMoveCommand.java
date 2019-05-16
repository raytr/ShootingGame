package game.client.commands;

import game.client.ClientCommand;
import game.client.ClientServerCommunicationCommand;
import game.client.Sprite;
import game.shared.net.messages.Command;
import game.shared.net.messages.commands.MoveCommand;

public class ClientMoveCommand implements ClientCommand, ClientServerCommunicationCommand {
    MoveCommand moveCommand;
    public ClientMoveCommand(MoveCommand mc){
        moveCommand =mc;
    }

    @Override
    public void execute(Sprite s) {
        if (moveCommand.getMoveRight() != null) s.setGoRight(moveCommand.getMoveRight());
        if (moveCommand.getMoveLeft() != null) s.setGoLeft(moveCommand.getMoveLeft());
        if (moveCommand.getMoveUp() != null) s.setGoUp(moveCommand.getMoveUp());
        if (moveCommand.getMoveDown() != null) s.setGoDown(moveCommand.getMoveDown());
    }

    @Override
    public Command getCommand() {
        return moveCommand;
    }
}
