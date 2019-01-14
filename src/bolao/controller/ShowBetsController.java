package bolao.controller;

import bolao.ConnectionFactory;
import bolao.Manager;
import bolao.model.Bet;
import bolao.model.BetWithName;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ShowBetsController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TableView<BetWithName> showPlayerTV;
    @FXML private TableColumn<BetWithName, String> namesColumn;
    @FXML private TableColumn<BetWithName, String> betsColumn;
    @FXML private TableColumn<BetWithName, String> remainingsColumn;
    @FXML private TableColumn<BetWithName, String> paidColumn;
    @FXML private Button payBtn;
    @FXML private TextField nameTF;
    @FXML private Button clearSearchBtn;
    @FXML private Button searchBets;
    @FXML private Button deleteBtn;
    @FXML private TextField newNmbrsTF;

    private Manager application;
    ObservableList<BetWithName> allBets;
    BetWithName bWN;
    int g_ID = 0;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void editBet(ActionEvent event){
        Bet b = new Bet();
        bWN = showPlayerTV.getSelectionModel().getSelectedItem();
        b.updateBet(bWN.getBet_ID(), newNmbrsTF.getText());
        reInitialize();
        newNmbrsTF.clear();
    }

    @FXML void confirmPayment(ActionEvent event) {
        Bet b = new Bet();
        bWN = showPlayerTV.getSelectionModel().getSelectedItem();
        b.confirmPayment(bWN.getBet_ID());
        reInitialize();
    }

    @FXML void dltBet(ActionEvent event) {
        Bet b = new Bet();
        bWN = showPlayerTV.getSelectionModel().getSelectedItem();
        b.rmvBet(bWN.getBet_ID());
        reInitialize();
    }

    @FXML void search(ActionEvent event) {
        String aux = nameTF.getText();
        for(BetWithName b: allBets){
            if (!b.getName().contains(aux)){
                allBets = bWN.getBetsWithName(g_ID, aux);
            }
        }
        showPlayerTV.getItems().clear();
        showPlayerTV.setItems(allBets);
    }

    @FXML void clear(ActionEvent event) {
        allBets = bWN.getBets(g_ID);
        showPlayerTV.setItems(allBets);
    }

    public  void reInitialize(){
        namesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        betsColumn.setCellValueFactory(new PropertyValueFactory<>("numbers"));
        remainingsColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));

        bWN = new BetWithName();

        Connection conn;
        PreparedStatement ps = null;
        String sql;
        ResultSet rs;

        try {
            sql = "SELECT * FROM sqlite_sequence WHERE name = (?)";

            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Game");
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("Name").equals("Game")) {
                    g_ID = Integer.parseInt(rs.getString("seq"));
                }
            }
            conn.close();
            allBets = bWN.getBets(g_ID);
            showPlayerTV.setItems(allBets);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        namesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        betsColumn.setCellValueFactory(new PropertyValueFactory<>("numbers"));
        remainingsColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));

        bWN = new BetWithName();

        Connection conn;
        PreparedStatement ps = null;
        String sql;
        ResultSet rs;

        try {
            sql = "SELECT * FROM sqlite_sequence WHERE name = (?)";

            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Game");
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("Name").equals("Game")) {
                    g_ID = Integer.parseInt(rs.getString("seq"));
                }
            }
            conn.close();
            allBets = bWN.getBets(g_ID);
            showPlayerTV.setItems(allBets);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setApp(Manager application){
        this.application = application;
    }
}