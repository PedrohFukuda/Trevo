/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolao.model;

import bolao.ConnectionFactory;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thayn
 */
public abstract class Checker {

    public static boolean checkForWinner(){
        int gameID = 0, drawID = 0;
        String sql = "SELECT name, seq FROM sqlite_sequence WHERE name = ? OR name = ?";
        Connection conn;
        PreparedStatement ps;
        boolean win = false;

        try {
            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Game");
            ps.setString(2, "Draw");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if (rs.getString("name").equals("Game")){
                    gameID = Integer.parseInt(rs.getString("seq"));
                } else if (rs.getString("name").equals("Draw")){
                    drawID = Integer.parseInt(rs.getString("seq"));
                }
            }

            conn.close();
            Draw d = new Draw("0");
            if (!d.checkIfOpen(drawID)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Correçao");
                alert.setHeaderText("Correçao ja efetuada");
                alert.setContentText("Esse sorteio ja foi corrigido");
                alert.showAndWait();
                return false;
            }
            win = makeCheck(gameID, drawID);

            if(win){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Checagem");
                alert.setHeaderText("Houve ganhadores");
                alert.setContentText("Jogo finalizado.");
                alert.showAndWait();
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Checagem");
                alert.setHeaderText("Nao houve ganhadores");
                alert.setContentText("Correçao efetuada com sucesso.");
                alert.showAndWait();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean makeCheck(int gameID, int drawID) {
        List<NumbersPerPlayer> playersBet = new ArrayList<NumbersPerPlayer>();
        List<Integer> winners = new ArrayList<Integer>();
        PreparedStatement ps;
        String numbers;

        playersBet = playersBetsSearch(gameID);
        numbers = findDraw(drawID);

        for (NumbersPerPlayer n : playersBet) {
            String h = checkHits(n.getNumbers(), numbers);

            Bet_Draw pd = new Bet_Draw(n.getBetID(), drawID, h);
            pd.registerBet_Draw();//

            Bet b = new Bet();
            if (b.updateRemaining(n.getBetID(), h)) {//
                winners.add(n.getBetID());
            }
        }

        try {
            int counter = 0;
            if (!winners.isEmpty()) {
                Connection conn;
                conn = ConnectionFactory.connect();
                String sql = "INSERT INTO games_Winners (game_ID, bet_ID) VALUES (?, ?)";

                for (int i : winners) {
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, gameID);
                    ps.setInt(2, winners.get(counter++));
                    ps.executeUpdate();
                }

                conn.close();
                return true;

            }

        } catch (SQLException ex) {
            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static String checkHits(String pNumbers, String dNumbers) {
        String hits = "";

        String[] auxText = pNumbers.split(" ");
        List<Integer> pn = new ArrayList<Integer>();
        for (String s : auxText) {
            if (!"".equals(s))
                pn.add(Integer.parseInt(s));
        }

        auxText = dNumbers.split(" ");
        List<Integer> dn = new ArrayList<Integer>();
        for (String s : auxText) {
            if(!"".equals(s))
                dn.add(Integer.parseInt(s));
        }

        for (int i : dn) {
            for (int k : pn) {
                if (i == k) {
                    hits = hits + i + " ";
                }
            }
        }

        return hits;
    }

    public static List<NumbersPerPlayer> playersBetsSearch(int game_ID) {
        List<NumbersPerPlayer> ids_bets = new ArrayList<NumbersPerPlayer>();
        ResultSet rs = null;

        String sql = "SELECT Player.player_ID, Bet.bet_ID, Bet.numbers FROM Player INNER JOIN Bet "
                + " ON Player.player_ID = Bet.player_ID AND Bet.game_ID = (?)";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);

            ps.setInt(1, game_ID);

            rs = ps.executeQuery();

            while (rs.next()) {
                NumbersPerPlayer aux = new NumbersPerPlayer(rs.getInt("player_ID"), rs.getInt("bet_ID"), rs.getString("numbers"));
                ids_bets.add(aux);
            }
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ids_bets;
    }

    public static String findDraw(int draw) {
        ResultSet rs = null;
        String drawNumbers = null;
        String sql = "SELECT numbers FROM Draw WHERE draw_ID = (?)";

        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, draw);

            rs = ps.executeQuery();

            drawNumbers = rs.getString(1);
            conn.close();



        } catch (SQLException ex) {
            Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return drawNumbers;
    }

}