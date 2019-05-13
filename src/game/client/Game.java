package game.client;

import game.client.net.ClientReceivePacketHandler;
import game.shared.net.NetHandler;
import game.shared.net.PacketSuccessFailHandler;
import game.shared.net.packets.LoginMsg;
import game.shared.net.packets.LogoutMsg;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.InetSocketAddress;

public class Game {
    private final NetHandler nh;
    private String username;
    private int playerNum = -1;

    public Game(InetSocketAddress serverAddr) {
        ClientReceivePacketHandler crph = new ClientReceivePacketHandler(this);
        nh = new NetHandler(crph, serverAddr);
    }

    public void tryLogin(String name, PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        username = name;
        nh.sendMessage(LoginMsg.encode(name, -1), onSuccess, onFailure);
    }

    public void show(Stage stage) {
        stage.setTitle("HBox Experiment 1");
        Label label = new Label("Not clicked");
        Button button = new Button("Click");

        button.setOnAction(value -> {
            label.setText("Clicked!");
        });
        HBox hbox = new HBox(button, label);
        Scene scene = new Scene(hbox, 200, 100);
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
}
