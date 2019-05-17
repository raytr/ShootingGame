package game.client;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class PlayerTableView {
    private TableView tv;

    @SuppressWarnings("unchecked")
    public PlayerTableView(ObservableList<Player> playerList){
        tv = new TableView();

        TableColumn<Player, Integer> column1 = new TableColumn<Player,Integer>("Player ID:");
        column1.setCellValueFactory(new PropertyValueFactory<Player,Integer>("playerId"));
        //tv.setStyle("-fx-opacity:1.0");
        //tv.setDisable(true);

        TableColumn<Player, String> column2 = new TableColumn<Player,String>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));

        TableColumn<Player, Integer> column3 = new TableColumn<Player,Integer>("Score");
        column3.setCellValueFactory(p -> p.getValue().scoreProperty().asObject() );

        tv.setSelectionModel(null);
        tv.setFocusTraversable(false);


        tv.setMouseTransparent(true);

        tv.getColumns().add(column1);
        tv.getColumns().add(column2);
        tv.getColumns().add(column3);

        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        /*
        SortedList<Player> sortedList = new SortedList<Player>(playerList,(Player p1,Player p2) -> {
            if (p1.getPlayerId() > p2.getPlayerId()){
                return 1;
            }else if (p1.getPlayerId() < p2.getPlayerId()){
                return -1;
            }else{
                return 0;
            }

        });

         */


        SortedList<Player> sortedPlayerList =  new SortedList<Player>(playerList,(Player p1,Player p2) -> {
            System.out.println("WE IN HERE");
            if (p1.getScore() < p2.getScore()){
                return 1;
            }else if (p1.getScore() > p2.getScore()){
                return -1;
            }else{
                return 0;
            }

        });


        //sortedPlayerList.comparatorProperty().bind(tv.comparatorProperty());

        //tv.setItems(playerList);
        tv.setItems(sortedPlayerList);
        //tv.getColumns().addAll(column1, column2);
    }
    public TableView getTable(){return tv;}
}
