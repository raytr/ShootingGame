package game.client;

import game.client.commands.ClientMoveCommand;
import game.shared.net.messages.Command;
import game.shared.net.messages.commands.MoveCommand;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.*;

public class ControlBinder {
    private float shootAngle = 0;
    private boolean isShooting = false;
    private Game g;
    private Node controlPane;
    private Map<KeyCode, ClientCommand[]> keyCodeCommandMap = new HashMap<>();
    //Binds controls to a specific pane
    public ControlBinder(Game g,Node p){
        this.g = g;
        controlPane = p;
        int playerNum = g.getPlayerNum();
        keyCodeCommandMap.put(KeyCode.W,new ClientCommand[]{
                new ClientMoveCommand(MoveCommand.encode(true,null,null,null)),
                new ClientMoveCommand(MoveCommand.encode(false,null,null,null))
        });
        keyCodeCommandMap.put(KeyCode.S,new ClientCommand[]{
                new ClientMoveCommand(MoveCommand.encode(null,true,null,null)),
                new ClientMoveCommand(MoveCommand.encode(null,false,null,null))
        });
        keyCodeCommandMap.put(KeyCode.D,new ClientCommand[]{
                new ClientMoveCommand(MoveCommand.encode(null,null,null,true)),
                new ClientMoveCommand(MoveCommand.encode(null,null,null,false))
        });
        keyCodeCommandMap.put(KeyCode.A,new ClientCommand[]{
                new ClientMoveCommand(MoveCommand.encode(null,null,true,null)),
                new ClientMoveCommand(MoveCommand.encode(null,null,false,null))
        });


        //Bind commands
        controlPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        controlPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyCodeCommandMap.containsKey(event.getCode())){
                    g.handlePlayerCommand(keyCodeCommandMap.get(event.getCode())[0]);
                }
            }
        });

        controlPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyCodeCommandMap.containsKey(event.getCode())){
                    g.handlePlayerCommand(keyCodeCommandMap.get(event.getCode())[1]);
                }
            }
        });

    }
}
