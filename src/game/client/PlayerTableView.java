package game.client;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlayerTableView {
    private TableView tv;

    public PlayerTableView(ObservableList<Player> playerList){
        tv = new TableView();

        TableColumn<String, Player> column1 = new TableColumn<>("Player ID:");
        column1.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        //tv.setStyle("-fx-opacity:1.0");
        //tv.setDisable(true);

        tv.setFocusTraversable(false);
        TableColumn<String, Player> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        tv.setSelectionModel(null);
        tv.getColumns().add(column1);
        tv.getColumns().add(column2);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        SortedList<Player> sortedList = new SortedList<Player>(playerList,(Player p1,Player p2) -> {
            if (p1.getPlayerId() > p2.getPlayerId()){
                return 1;
            }else if (p1.getPlayerId() < p2.getPlayerId()){
                return -1;
            }else{
                return 0;
            }

        });


        //tv.setItems(playerList);
        tv.setItems(sortedList);
        //tv.getColumns().addAll(column1, column2);
    }
    public TableView getTable(){return tv;}
}
