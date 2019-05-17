package game.client;

import game.client.net.ClientReceivePacketHandler;
import game.shared.net.NetManager;
import game.shared.net.Packet;
import game.shared.net.PacketSuccessFailHandler;
import game.shared.net.messages.CommandMsg;
import game.shared.net.messages.LoginMsg;
import game.shared.net.messages.LogoutMsg;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.InetSocketAddress;
import java.util.List;

public class Game {
    private Playfield playfield = new Playfield(this);
    private BorderPane mainPane;
    private ChatBox chatBox;
    private final NetManager nh;
    private String username;
    private int playerNum = -1;
    private ObservableList<Player> playerList = FXCollections.observableArrayList((p ->
            new Observable[] {p.scoreProperty()}));


    private PlayerTableView ptv;
    Player clientPlayer;

    public Game(InetSocketAddress serverAddr) {
        ClientReceivePacketHandler crph = new ClientReceivePacketHandler(this);
        nh = new NetManager(crph, serverAddr);
        playerList.addListener(new ListChangeListener<Player>() {
            @Override
            public void onChanged(Change<? extends Player> c) {
                System.out.println("SOMETHING CHANGED");
            }
        });
    }

    public void tryLogin(String name, PacketSuccessFailHandler onSuccess, PacketSuccessFailHandler onFailure) {
        username = name;
        nh.sendMessage(LoginMsg.encode(name, -1), onSuccess, onFailure);
    }

    public void show(Stage stage) {
        stage.setTitle("SHOOTER GAME CLIENT");
        mainPane = new BorderPane();

        //Set up scene
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int screenWidth = (int)primaryScreenBounds.getWidth();
        int screenHeight = (int)primaryScreenBounds.getHeight();
        screenWidth /= 1.5;
        screenHeight /= 1.2;
        Scene scene = new Scene(mainPane, screenWidth, screenHeight);




        //Set up right-hand side bar playerview and chat box
        ptv = new PlayerTableView(playerList);
        ptv.getTable().setPrefHeight(screenHeight/2);
        chatBox = new ChatBox(screenWidth/5,screenHeight/2,this,nh);
        VBox vbox = new VBox();
        vbox.getChildren().add(ptv.getTable());
        vbox.getChildren().add(chatBox.getBody());
        mainPane.setRight(vbox);


        //Set up left-hand playfield
        playfield.init(screenWidth - screenWidth/5,screenHeight);
        mainPane.setLeft(playfield.getBody());


        ControlBinder cb = new ControlBinder(this,mainPane);

        mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainPane.requestFocus();
            }
        });
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                close();
            }
        });
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
    public void setClientPlayer(Player p){
        clientPlayer = p;
    }

    public Player addPlayer(int playerNum, String name) {
        //System.out.println("ADDING " + playerNum + " " +name);
        for (Player p : playerList){
            if (p.getPlayerId() == playerNum) return null;
        }
        Player newPlayer = new Player(playerNum,name);

        playerList.add(newPlayer);
        return newPlayer;
        /*
        Sprite newSprite = new Sprite();
        newSprite.setName(newPlayer.getName());
        newSprite.setId(newPlayer.getPlayerId());
        newPlayer.setSprite(newSprite);
        playfield.addSprite(newSprite);
        if (playerNum == this.getPlayerNum()){
            playfield.bindCameraToSprite(newSprite);
        }

         */

    }
    public Player getClientPlayer(){
        return clientPlayer;
    }
    public void removePlayer(int playerNum){
        for (int i=playerList.size() - 1;i>=0;i--){
            if (playerList.get(i).getPlayerId() == playerNum){
                Player p = playerList.get(i);
                playerList.remove(i);
                return;
            }
        }
    }
    public List<Player> getPlayerList(){return playerList;}


    public Player getPlayer(int playerNum) {
        for (Player p : playerList){
            if (p.getPlayerId() == playerNum) return p;
        }
        return null;
    }

    public void handlePlayerCommand(ClientCommand m) {
        m.execute(playfield.getCameraBoundedSprite());
        if(m instanceof ClientServerCommunicationCommand)
            nh.sendMessage(CommandMsg.encode(playerNum,((ClientServerCommunicationCommand) m).getCommand()));
    }

    public ChatBox getChatBox(){
        return chatBox;
    }
    public Playfield getPlayfield(){return playfield;}
    private void close(){
        PacketSuccessFailHandler psfh = new PacketSuccessFailHandler() {
            @Override
            public void run(InetSocketAddress addr) {
                playfield.stop();
                nh.stop();
            }
        };
        nh.sendMessage(LogoutMsg.encode(playerNum), psfh, psfh);
    }
    public PlayerTableView getPtv(){
        return ptv;
    }
}
