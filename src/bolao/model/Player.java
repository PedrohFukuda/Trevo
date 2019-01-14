package bolao.model;

import bolao.ConnectionFactory;
import javafx.scene.control.Alert;
import sun.misc.GC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thayna
 */
public class Player {
    private String name;
    private String address;
    private String phone;
    private String email;

    //CONTRUCTORS
    public Player(String name, String address, String phone, String email) {
        setName(name);
        setAddress(address);
        setPhone(phone);
        setEmail(email);
    }

    public Player(String name){
        setName(name);
        setAddress("");
        setPhone("");
        setEmail("");
    }

    //DATABASE SEARCHS
    public List searchByName(String name){
        ResultSet rs = null;
        String sql = "SELECT player_ID, name FROM Player WHERE name LIKE ?";


        PreparedStatement ps;
        List<String> names = new ArrayList<String>();
        try {
            Connection conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while(rs.next()){
                names.add(rs.getString("name"));
            }

            conn.close();

            return names;

        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        return names;
    }

    public int searchID(String name) {
        int i = -1;
        ResultSet rs = null;
        String sql = "SELECT player_ID FROM Player WHERE name = ?";

        PreparedStatement ps;
        try {
            Connection conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            i = rs.getInt("player_ID");
            conn.close();
            return i;
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }


        return i;
    }

    public Player searchByID(int p_ID){
        Player p = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Player WHERE player_ID = ?";

        PreparedStatement ps;
        try {
            Connection conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, p_ID);
            rs = ps.executeQuery();
            p = new Player(rs.getString("name"), rs.getString("address"), rs.getString("email"), rs.getString("phone"));
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }


        return p;
    }

    //DATABASE UPDATERS
    public void registerNewPlayer(){
        String sql = "INSERT INTO Player(name, address, email, phone) VALUES (?,?,?,?)";

        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setString(2, this.address);
            ps.setString(3, this.email);
            ps.setString(4, this.phone);
            ps.executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePlayer(int p_ID){
        String sql = "UPDATE Player set name = ?, address = ?, email = ?, phone = ? WHERE player_id = ?";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setString(2, this.address);
            ps.setString(3, this.phone);
            ps.setString(4, this.email);
            ps.setInt(5, p_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rmvPlayer(int p_ID) {
        String sql = "DELETE FROM Player WHERE player_ID = ?";
        try{
            try {
                Connection conn = ConnectionFactory.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, p_ID);
                ps.executeUpdate();
                conn.close();
            }   catch (NullPointerException n){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alerta");
                alert.setHeaderText("Clique em (Selecionar) antes de deletar um jogador");
                alert.showAndWait();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    //GETTERS
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    //SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
