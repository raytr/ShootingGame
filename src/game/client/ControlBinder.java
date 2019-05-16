package game.client;

import game.client.commands.ClientChangeAngleCommand;
import game.client.commands.ClientMoveCommand;
import game.client.commands.ClientSetShootingCommand;
import game.shared.net.messages.commands.ChangeAngleCommand;
import game.shared.net.messages.commands.MoveCommand;
import game.shared.net.messages.commands.SetShootingCommand;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
        controlPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sendAngle(event);
            }
        });
        controlPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sendAngle(event);
            }
        });


        //Bind commands
        controlPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                g.handlePlayerCommand(new ClientSetShootingCommand(SetShootingCommand.encode(true)));
            }
        });

        controlPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                g.handlePlayerCommand(new ClientSetShootingCommand(SetShootingCommand.encode(false)));
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

    private void sendAngle(MouseEvent event){
        double x0 = 0;
        double y0 = 0;
        boolean useZero = false;
        try{
            Sprite sprite = g.getClientPlayer().getSprite();
            x0 = g.getPlayfield().getWidth()/2;
            y0 = g.getPlayfield().getHeight()/2;
        }catch(NullPointerException npe){
            //Do nothing; the sprite hasn't been bound yet
            System.out.println("Expecting this");
            useZero = true;
        }
        double angle = 0;
        if (!useZero) angle = getAngle(x0,y0, event.getX(),event.getY());
        g.handlePlayerCommand(new ClientChangeAngleCommand(ChangeAngleCommand.encode((float)angle)));
    }
    private double getAngle(double x1, double y1, double x2, double y2) {
        float angle = (float) (Math.atan2(y2 - y1, x2 - x1));
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }
}
