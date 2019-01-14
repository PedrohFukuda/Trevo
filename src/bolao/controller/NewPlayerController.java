package bolao.controller;

import bolao.Manager;
import bolao.model.Player;
import com.itextpdf.text.api.Indentable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewPlayerController implements Initializable {

    @FXML private Button returnBtn;
    @FXML private TextField nameTF;
    @FXML private TextField phoneTF;
    @FXML private TextField emailTF;
    @FXML private TextField adressTF;
    @FXML private Button registerBtn;
    @FXML private Label newPlayerMsg;

    private Manager application;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void registerPlayer(ActionEvent event) {
        if (!"".equals(nameTF.getText())){
            Player player = new Player(nameTF.getText(), adressTF.getText(), phoneTF.getText(), emailTF.getText());
            player.registerNewPlayer();
            nameTF.clear();
            adressTF.clear();
            phoneTF.clear();
            emailTF.clear();
        }
        newPlayerMsg.setText("Novo Jogador Cadastrado Com Sucesso");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(Manager application){
        this.application = application;
    }
}
