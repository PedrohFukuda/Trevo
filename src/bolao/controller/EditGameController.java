package bolao.controller;

import bolao.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class EditGameController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private Button reopenForBetsBtn;
    @FXML private Button reopenGameBtn;

    private Manager application;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void reopenBets(ActionEvent event) {

    }

    @FXML void reopenGame(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(Manager application){
        this.application = application;
    }
}
