package bolao.controller;

import bolao.Manager;
import bolao.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class NewUserController implements Initializable {

    private Manager application;

    @FXML
    private Button returnBtn;

    @FXML
    private TextField userTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Button registerBtn;

    @FXML
    private Label newUserMsg;

    @FXML
    void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML
    void registerUser(ActionEvent event) {
        User newUser = new User(userTF.getText(), passwordTF.getText());
        newUser.registerNewUser();
        newUserMsg.setText("Novo Usuario Cadastrado com Sucesso");

    }

    public void setApp(Manager application){
        this.application = application;
    }

    @FXML
    void registerUser(KeyEvent event) {
        User newUser = new User(userTF.getText(), passwordTF.getText());
        newUser.registerNewUser();
        newUserMsg.setText("Novo Usuario Cadastrado com Sucesso");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
