package bolao.controller;

import bolao.ConnectionFactory;
import bolao.Manager;
import bolao.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewGameController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TextField gameSizeTF;
    @FXML private Button confirmBtn;
    @FXML private Label newGameMsg;

    private Manager application;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void registerGame(ActionEvent event) {
        String aux = gameSizeTF.getText();

        if(!"".equals(aux) && aux.matches("[0-9]+")){
            Game game = new Game(Integer.parseInt(gameSizeTF.getText()));
            game.registerNewGame();
            gameSizeTF.clear();
            newGameMsg.setText("Novo Jogo Iniciado");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Impossível cadastrar");
            alert.setContentText("Encerre o jogo atual para continuar.");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setApp(Manager application){
        this.application = application;
    }
}
