package game.client;


import game.shared.net.NetManager;
import game.shared.net.messages.ChatMsg;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatBox {
    private TextFlow chatPane;
    private TextField inputField;
    private Pane body;
    private NetManager nm;
    private Game g;
    public ChatBox(Game g,NetManager nm){
        this.g = g;
        this.nm = nm;
        body = new VBox();
        chatPane = new TextFlow();
        inputField = new TextField();
        inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    nm.sendMessage(ChatMsg.encode(inputField.getText(),g.getPlayerNum()));
                    inputField.setText("");
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane(chatPane);
        chatPane.setPrefHeight(400);
        chatPane.setPrefWidth(200);

        // removing this line will allow the scroll pane to grow
        // to an unlimited width, and so will prevent the text from
        // wrapping:
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.vvalueProperty().bind(chatPane.heightProperty());
        body.getChildren().add(scrollPane);
        body.getChildren().add(inputField);

    }
    public void addMsg(int playerNum,String m,boolean serverMsg){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text t;
                if (serverMsg) {
                    t = new Text(m+ "\n");
                    t.setFill(Color.RED);
                }else t = new Text( g.getPlayer(playerNum).getName() + "#" + g.getPlayer(playerNum).getPlayerId()+": "+  m + "\n");
                chatPane.getChildren().add(t);
            }
        });
    }

    public Node getBody() {
        return body;
    }
}
