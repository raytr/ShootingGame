package game.client;

import game.client.model.Player;
import game.client.net.ClientReceivePacketHandler;
import game.shared.net.NetManager;
import game.shared.net.PacketSuccessFailHandler;
import game.shared.net.messages.LoginMsg;
import game.shared.net.messages.LogoutMsg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.InetSocketAddress;

public class Game {
    private ChatBox chatBox;
    private final NetManager nh;
    private String username;
    private int playerNum = -1;
    private ObservableList<Player> playerList = FXCollections.observableArrayList();

    public Game(InetSocketAddress serverAddr) {
        ClientReceivePacketHandler crph = new ClientReceivePacketHandler(this);
        nh = new NetManager(crph, serverAddr);
    }

    public void tryLogin(String name, PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        username = name;
        nh.sendMessage(LoginMsg.encode(name, -1), onSuccess, onFailure);
    }

    public void show(Stage stage) {
        stage.setTitle("HBox Experiment 1");

        PlayerTableView ptv = new PlayerTableView(playerList);

        chatBox = new ChatBox(this,nh);

        Label label = new Label("Not clicked");
        Button button = new Button("Click");

        button.setOnAction(value -> {
            label.setText("Clicked!");
        });
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        hbox.getChildren().add(button);
        vbox.getChildren().add(ptv.getTable());
        vbox.getChildren().add(chatBox.getBody());
        hbox.getChildren().add(vbox);


        Scene scene = new Scene(hbox, 800, 800);
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                nh.sendMessage(LogoutMsg.encode(playerNum), new PacketSuccessFailHandler() {
                    @Override
                    public void run(InetSocketAddress addr) {

                        nh.stop();
                    }
                }, new PacketSuccessFailHandler() {
                    @Override
                    public void run(InetSocketAddress addr) {
                        nh.stop();
                    }
                });
            }
        });
        stage.show();
    }

    public String getPlayerName() {
        return username;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int s) {
        playerNum = s;
    }

    public void addPlayer(int playerNum, String name) {
        //System.out.println("ADDING " + playerNum + " " +name);
        for (Player p : playerList){
            if (p.getPlayerId() == playerNum) return;
        }
        playerList.add(new Player(playerNum,name));
    }
    public void removePlayer(int playerNum){
        for (int i=playerList.size() - 1;i>=0;i--){
            if (playerList.get(i).getPlayerId() == playerNum){
                playerList.remove(i);
                return;
            }
        }
    }

    public ChatBox getChatBox(){
        return chatBox;
    }

    public Player getPlayer(int playerNum) {
        for (Player p : playerList){
            if (p.getPlayerId() == playerNum) return p;
        }
        return null;
    }
}
