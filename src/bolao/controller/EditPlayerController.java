package bolao.controller;

import bolao.Manager;
import bolao.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditPlayerController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TextField playerNameTF;
    @FXML private Button searchPlyrBtn;
    @FXML private ListView<String> showPlayerLV;
    @FXML private TextField nameTF;
    @FXML private TextField phoneTF;
    @FXML private TextField emailTF;
    @FXML private TextField adressTF;
    @FXML private Button updateBtn;
    @FXML private Button clearSearchBtn;
    @FXML private Button selectPlayerBtn;
    @FXML private Button deleteBtn;

    private Manager application;
    Player p;
    int player_ID;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void searchPlayer(ActionEvent event) {
        showPlayerLV.getItems().clear();
        Player player = new Player("search");
        List<String> al = player.searchByName(playerNameTF.getText());


        for(String s: al){
            showPlayerLV.getItems().add(s);
        }
    }

    @FXML void selectPlayer(ActionEvent event) {
        p = new Player("busca");

        String aux = (String) showPlayerLV.getSelectionModel().getSelectedItem();

        player_ID = p.searchID(aux);
        System.out.println(player_ID);
        p = p.searchByID(player_ID);
        nameTF.setText(p.getName());
        phoneTF.setText(p.getPhone());
        emailTF.setText(p.getEmail());
        adressTF.setText(p.getAddress());

    }

    @FXML void updatePlayer(ActionEvent event) {
        p = new Player(nameTF.getText(), adressTF.getText(), phoneTF.getText(), emailTF.getText());
        p.updatePlayer(player_ID);

        nameTF.clear();
        phoneTF.clear();
        emailTF.clear();
        adressTF.clear();
    }

    @FXML void deletePlayer(ActionEvent event) {
        p.rmvPlayer(player_ID);

        nameTF.clear();
        phoneTF.clear();
        emailTF.clear();
        adressTF.clear();

        reInitialize();
    }

    @FXML void clearSearch(ActionEvent event) {
        showPlayerLV.getItems().clear();
        Player player = new Player("search");
        List<String> al = player.searchByName("%");


        for(String s: al){
            showPlayerLV.getItems().add(s);
        }
    }

    public void reInitialize(){
        showPlayerLV.getItems().clear();
        Player player = new Player("search");
        List<String> al = player.searchByName("%");


        for(String s: al){
            showPlayerLV.getItems().add(s);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPlayerLV.getItems().clear();
        Player player = new Player("search");
        List<String> al = player.searchByName("%");


        for(String s: al){
            showPlayerLV.getItems().add(s);
        }
    }

    public void setApp(Manager application){
        this.application = application;
    }
}
