package game;

import game.client.Game;
import game.server.GameServer;
import game.shared.net.PacketSuccessFailHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.InetSocketAddress;

public class GameLauncher extends Application {
    private GridPane grid;
    private GameServer gameServer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text client = new Text("Client");
        client.setFont(Font.font("Arial", 40));
        grid.add(client, 1, 1);
        GridPane.setHalignment(client, HPos.CENTER);

        Label ipBox = new Label("IP:");
        grid.add(ipBox, 0, 2);

        TextField ipField = new TextField();
        grid.add(ipField, 1, 2);

        Label portBox = new Label("Port:");
        grid.add(portBox, 0, 3);

        TextField portField = new TextField();
        grid.add(portField, 1, 3);

        Label userName = new Label("Username");
        grid.add(userName, 0, 4);

        TextField userField = new TextField();
        grid.add(userField, 1, 4);

        Button btn = new Button("Join");
        grid.add(btn, 0, 5);


        Label errMsg = new Label("");

        errMsg.setFont(Font.font("Arial", 12));
        grid.add(errMsg, 1, 6);
        // Set font color for the Label.
        errMsg.setTextFill(Color.rgb(255, 0, 0));

        Label portBox2 = new Label("Port:");
        grid.add(portBox2, 5, 2);

        Text server = new Text("Server");
        server.setFont(Font.font("Arial", 40));
        grid.add(server, 6, 1);
        GridPane.setHalignment(server, HPos.CENTER);

        TextField portField2 = new TextField();
        grid.add(portField2, 6, 2);

        Button btn2 = new Button("Make a server");
        grid.add(btn2, 6, 3);

        Label errMsg2 = new Label("");
        errMsg2.setFont(Font.font("Arial", 12));
        grid.add(errMsg2, 6, 4);
        // Set font color for the Label.
        errMsg2.setTextFill(Color.rgb(255, 0, 0));

        GameLauncher gl = this;
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                errMsg.setText("");
                if (!(ipField.getText().equals("")) && !(portField.getText().equals("")) && !(userField.getText().equals(""))) {
                    String ip = ipField.getText();
                    int port = 1;
                    try {
                        port = Integer.parseInt(portField.getText());
                    } catch (NumberFormatException e2) {
                        errMsg.setText("Please enter a valid port.");
                        return;
                    }
                    String name = userField.getText();
                    System.out.println("IP: " + ip + " PORT: " + port + " NAME: " + name);
                    Game game = new Game(new InetSocketAddress(ip, port));
                    game.tryLogin(name, new PacketSuccessFailHandler() {
                        @Override
                        public void run(InetSocketAddress addr) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("Successfully logged in to " + addr);
                                    game.show(new Stage());
                                }
                            });
                        }
                    }, new PacketSuccessFailHandler() {
                        @Override
                        public void run(InetSocketAddress addr) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    errMsg.setText("Login to " + addr + "failed! Check ip/port");
                                }
                            });
                        }
                    });
                }
            }
        });

        btn2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                errMsg2.setText("");
                if (!portField2.getText().equals("")) {
                    int port = -1;
                    try {
                        port = Integer.parseInt(portField2.getText());
                    } catch (NumberFormatException e2) {
                        errMsg2.setText("Please enter a valid port");
                        return;
                    }
                    if (btn2.getText().equals("Make a server")) {
                        gameServer = new GameServer(port);
                        gameServer.init();
                        btn2.setText("Stop server");
                        portField2.setDisable(true);
                    } else {
                        gameServer.stop();
                        btn2.setText("Make a server");
                        portField2.setDisable(false);
                    }
                }
            }
        });

        Scene scene = new Scene(grid, 700, 500);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (gameServer != null) gameServer.stop();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public GridPane getGrid() {
        return grid;
    }
}
