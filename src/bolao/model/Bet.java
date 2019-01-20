/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolao.model;

import bolao.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

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
public final class Bet{
    private int player_ID;
    private int game_ID;
    private String numbers;
    private String remaining;
    public int size;


    public Bet(int player_ID, String numbers) {
        setPlayer_ID(player_ID);
        setGame_ID();
        setNumbers(numbers);
        this.remaining = numbers;
        String[] aux = numbers.split(" ");
        this.size = 0;
        for (String s : aux) {
            if (!s.equals("")) {
                this.size++;
            }
        }
    }

    public Bet(){

    }

    public String getRemaining(){
        return remaining;
    }

    public int getPlayer_ID() {
        return player_ID;
    }

    public void setPlayer_ID(int player_ID) {
        String sql = "SELECT * FROM Player WHERE player_ID = ?";
        int i = 0;
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, player_ID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                i++;
            }
            if(i == 1)
                this.player_ID = player_ID;

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(Bet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getGame_ID() {
        return game_ID;
    }

    public void setGame_ID() {
        String sql = "SELECT seq AS id FROM sqlite_sequence WHERE name = ?";
        int i = 0;
        String dateAux = "";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "Game");
            ResultSet rs = ps.executeQuery();

            this.game_ID = rs.getInt("id");
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(Bet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public void registerNewBet(){
        if (player_ID != 0 && game_ID != 0){
            try{
                String sql = "INSERT INTO Bet" +
                        " (player_ID, game_ID, numbers, remaining, paid, remainingSize, originalSize)" +
                        " VALUES(?, ?, ?, ?, ?, ?, ?)";
                Connection conn = ConnectionFactory.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, this.player_ID);
                ps.setInt(2, this.game_ID);
                ps.setString(3, this.numbers);
                ps.setString(4, this.numbers);
                ps.setInt(5, 0);
                ps.setInt(6, this.size);
                ps.setInt(7, this.size);
                System.out.println(player_ID);
                System.out.println(game_ID);
                System.out.println(numbers);
                ps.executeUpdate();
                conn.close();

            } catch(SQLException e) {
                System.out.println("Problema ao registrar uma aposta" +"\n" + e.getMessage());
            }
        } else {
            //PRINT ERROR MESSAGE
        }
    }

    public boolean updateRemaining(int bID, String rmv){
        int originalSize;
        int rmngSize = 0;
        boolean winner = false;
        String[] aux = rmv.split(" ");
        List<Integer> rmvNumbers = new ArrayList<>();
        List<Integer> newRemaining = new ArrayList<>();
        String nR = "";

        for (String s: aux) {
            if(!s.equals("")) {
                rmvNumbers.add(Integer.parseInt(s));
            }
        }

        try {
            String sql = "SELECT remaining, originalSize FROM Bet WHERE bet_ID = ?";
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bID);
            ResultSet rs = ps.executeQuery();

            aux = rs.getString("remaining").split(" ");
            originalSize = rs.getInt("originalSize");

            for (String s: aux) {
                if (!s.equals("")) {
                    newRemaining.add(Integer.parseInt(s));
                }
            }

            int numberIsPresent = 0;
            for (int i: newRemaining){
                numberIsPresent = 0;
                for(int k: rmvNumbers){
                    if (i == k) {
                        numberIsPresent = 1;
                    }
                }
                if (numberIsPresent == 0){
                    nR = nR + i + " ";
                    rmngSize++;
                }
            }

            sql = "UPDATE Bet SET remaining = ?, remainingSize = ? WHERE bet_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nR);
            ps.setInt(2, (originalSize - rmngSize));
            ps.setInt(3, bID);
            ps.executeUpdate();
            conn.close();

            if (nR.equals(""))
                winner = true;
        } catch (SQLException ex) {
            Logger.getLogger(Bet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return winner;
    }

    public void updateBet(int b_ID, String s){
        String sql = "UPDATE Bet set numbers = ?, remaining = ? WHERE bet_ID = ?";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            ps.setString(2, s);
            ps.setInt(3, b_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rmvBet(int b_ID) {
        String sql = "DELETE FROM Bet WHERE bet_ID = ?";
        try{
            try {
                Connection conn = ConnectionFactory.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, b_ID);
                ps.executeUpdate();
                conn.close();
            }   catch (NullPointerException n){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alerta");
                alert.setHeaderText("Clique em (Selecionar) antes de deletar uma aposta");
                alert.showAndWait();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void confirmPayment(int b_ID) {
        String p = "0";
        Connection conn = ConnectionFactory.connect();
        String sql = "SELECT paid FROM Bet WHERE bet_ID = ?";
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, b_ID);
            rs = ps.executeQuery();
            while(rs.next()) {
                p = rs.getString("paid");
                System.out.println(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        sql = "UPDATE Bet SET paid = ? WHERE bet_ID = ?";


        try {
            ps = conn.prepareStatement(sql);
            if (p.equals("1")) {
                ps.setString(1, "0");
            } else {
                ps.setString(1, "1");
            }
            ps.setInt(2, b_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}