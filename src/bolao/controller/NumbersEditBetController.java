package bolao.controller;

import bolao.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NumbersEditBetController implements Initializable {
    @FXML private TextField numbersTF;
    @FXML private Button numbersBTN;
    @FXML private Button returnBtn;

    private Manager application;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML
    void updateBet(ActionEvent event) {

    }

    public void setApp(Manager application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}