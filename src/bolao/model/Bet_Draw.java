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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thayn
 */
public class Bet_Draw {
    private int bet_ID;
    private int draw_ID;
    private int game_ID;
    private int hit_size;
    private String hits;

    public Bet_Draw(int bID, int dID, String n){
        setBet_ID(bID);
        setDrawAndGame_IDs(dID);
        setHits(n);
    }

    public int getBet_ID() {
        return bet_ID;
    }

    public void setBet_ID(int bet_ID) {
        String sql = "SELECT * FROM Bet WHERE bet_ID = ?";
        int i = 0;
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, bet_ID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                i++;
            }

            conn.close();
            if(i == 1)
                this.bet_ID = bet_ID;

        } catch (SQLException ex) {
            Logger.getLogger(Bet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getDraw_ID() {
        return draw_ID;
    }

    public void setDrawAndGame_IDs(int draw_ID) {
        String sql = "SELECT * FROM Draw WHERE draw_ID = ?";
        int i = 0, auxGame_ID = 0;
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, draw_ID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                i++;
                auxGame_ID = rs.getInt("game_ID");
            }
            if(i == 1){
                this.draw_ID = draw_ID;
                this.game_ID = auxGame_ID;
            }

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Bet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getGame_ID() {
        return game_ID;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        String[] aux = hits.split(" ");
        this.hit_size = 0;

        for (String j : aux) {
            this.hit_size++;
        }

        this.hits = hits;
    }

    public void registerBet_Draw(){
        if(this.draw_ID != 0 && this.bet_ID != 0){
            String sql = "INSERT INTO Bet_Draw (bet_ID, draw_ID, game_ID, hitNumbers, hitSize) VALUES (?, ?, ?, ?, ?)";
            Connection conn;
            try {
                conn = ConnectionFactory.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, this.bet_ID);
                ps.setInt(2, this.draw_ID);
                ps.setInt(3, this.game_ID);
                ps.setString(4, this.hits);
                ps.setInt(5, this.hit_size);
                ps.executeUpdate();
                conn.close();

            } catch (SQLException ex) {
                Logger.getLogger(Bet_Draw.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


}