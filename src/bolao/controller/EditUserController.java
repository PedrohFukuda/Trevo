package bolao.controller;

import bolao.ConnectionFactory;
import bolao.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TextField userTF;
    @FXML private Button clearSearchBtn;
    @FXML private Button searchPlyrBtn;
    @FXML private Button selectPlayerBtn;
    @FXML private ListView<String> showUserLV;
    @FXML private TextField newPassTF;
    @FXML private Button deleteBtn;
    @FXML private Button updateBtn;

    private Manager application;
    String user;

    @FXML void clearSearch(ActionEvent event) {
        showUserLV.getItems().clear();
        String sql = "SELECt username FROM users WHERE username LIKE ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                showUserLV.getItems().add(rs.getString("username"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML void deleteUser(ActionEvent event) {

        String sql = "DELETE FROM users WHERE username = ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.executeUpdate();
            conn.close();
            System.out.println(conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showUserLV.getItems().clear();
        reInitialize();
    }

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void searchUser(ActionEvent event) {
        showUserLV.getItems().clear();
        String sql = "SELECt username FROM users WHERE username LIKE ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTF.getText());
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                showUserLV.getItems().add(rs.getString("username"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML void selectUser(ActionEvent event) {
        user = (String) showUserLV.getSelectionModel().getSelectedItem();
    }

    @FXML void updateUser(ActionEvent event) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassTF.getText());
            ps.setString(2, user);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reInitialize(){
        String sql = "SELECt username FROM users WHERE username LIKE ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                showUserLV.getItems().add(rs.getString("username"));
                System.out.println(rs.getString("username"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sql = "SELECT username FROM users WHERE username LIKE ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                showUserLV.getItems().add(rs.getString("username"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setApp(Manager application){
        this.application = application;
    }
}
