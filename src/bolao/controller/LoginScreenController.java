package bolao.controller;

import bolao.ConnectionFactory;
import bolao.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordTF;
    @FXML private Button loginBtn;

    private Manager application;

    @FXML void logon(ActionEvent event) {
        String usrnm, pswrd;

        usrnm = usernameTF.getText();
        pswrd = passwordTF.getText();

        String sql = "SELECT password FROM users WHERE username = ?";

        Connection conn;
        PreparedStatement ps;

        try {
            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usrnm);
            ResultSet rs = ps.executeQuery();

            if (rs.getString("password").equals(pswrd)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText("Login realizado");
                alert.setContentText("Seja bem vindo!");
                alert.showAndWait();
                this.application.goMainScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText("Usuario e/ou senha incorreto(s)");
                alert.setContentText("Por favor tente novamente.");
                alert.showAndWait();
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(Manager application){
        this.application = application;
    }
}