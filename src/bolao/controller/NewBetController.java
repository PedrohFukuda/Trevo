package bolao.controller;

import bolao.Manager;
import bolao.model.Bet;
import bolao.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewBetController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TextField playrNameTF;
    @FXML private Button searchBtn;
    @FXML private Button selectPlyrBtn;
    @FXML private ListView<String> showPlayerLV;
    @FXML private TextField betsNumbersTF;
    @FXML private Button registerBetBtn;
    @FXML private Label newBetMsg;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    private Manager application;
    int player_ID;


    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void registerBet(ActionEvent event) {
        String aux = betsNumbersTF.getText();

        if (!aux.equals("") && aux.matches("[[0-9]+ ]+[ [0-9]+]") && player_ID != -1) {
            Bet bet = new Bet(player_ID, betsNumbersTF.getText());
            bet.registerNewBet();
            betsNumbersTF.clear();
            newBetMsg.setText("Nova Aposta Registrada Com Sucesso");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Impossível cadastrar");
            alert.setContentText("Garanta que as informacoes estao corretas.");
            alert.showAndWait();

        }
    }

    @FXML void searchPlayer(ActionEvent event) {
        showPlayerLV.getItems().clear();
        Player player = new Player("search");
        List<String> al = player.searchByName(playrNameTF.getText());


        for(String s: al){
            showPlayerLV.getItems().add(s);
        }

    }

    @FXML void selectPlayer(ActionEvent event) {
        Player p = new Player("busca");

        String aux = (String) showPlayerLV.getSelectionModel().getSelectedItem();

        player_ID = p.searchID(aux);
        newBetMsg.setText("");

    }

    @FXML
    void editBet(ActionEvent event) {

    }
    @FXML
    void dltBet(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player_ID = -1;
    }

    public void setApp(Manager application){
        this.application = application;
    }
}
