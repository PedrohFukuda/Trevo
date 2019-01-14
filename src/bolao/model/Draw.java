/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolao.model;

import bolao.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thayna
 */
public class Draw {

    private int game_ID;
    private String sortingDate;
    private String numbers;

    public Draw(String numbers){
        setGame_ID();
        setNumbers(numbers);
        setSortingDate();
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

    public String getSortingDate() {
        return sortingDate;
    }

    public void setSortingDate() {
        LocalDate ld = LocalDate.now();
        String date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
        date = ld.format(formatter);
        this.sortingDate = date;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public void registerNewDraw(){
        String sql = "INSERT INTO Draw (numbers, game_ID, sortingDate) VALUES(?, ?, ?)";

        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.numbers);
            ps.setInt(2, this.game_ID);
            ps.setString(3, this.sortingDate);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkIfOpen(int d_ID){
        boolean b = false;
        String sql = "SELECT checked FROM Draw WHERE draw_ID = ?";

        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, d_ID);
            ResultSet rs = ps.executeQuery();

            if(rs.getString("checked").equals("0")){
                b = true;
                sql = "UPDATE Draw SET Checked = ? WHERE draw_ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "1");
                ps.setInt(2, d_ID);
                ps.executeUpdate();
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
        }

        return b;
    }
}