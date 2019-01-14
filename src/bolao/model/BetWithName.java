package bolao.model;

import bolao.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BetWithName {
    private String name, numbers, remaining, paid;
    int bet_ID;

    public BetWithName(String name, String numbers, String remaining, String paid, int bet_ID) {
        this.name = name;
        this.numbers = numbers;
        this.remaining = remaining;
        this.paid = paid;
        this.bet_ID = bet_ID;
    }

    public BetWithName() {
    }

    public ObservableList<BetWithName> getBetsWithName(int g_ID, String partialName){
        ObservableList<BetWithName> ol = FXCollections.observableArrayList();

        String sql = "SELECT Player.name, Bet.numbers, Bet.remaining, Bet.paid, Bet.bet_ID "
                + "FROM Bet "
                + "INNER JOIN Player ON Bet.player_ID = Player.player_ID "
                + "WHERE game_ID = ? AND Player.name LIKE ?";

        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, g_ID);
            ps.setString(2, "%" + partialName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String aux = "";
                if (rs.getString("paid").equals("1"))
                    aux = "PG";
                ol.add(new BetWithName(rs.getString("name"), rs.getString("numbers"), rs.getString("remaining"), aux, Integer.parseInt(rs.getString("bet_ID"))));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ol;

    }

    public ObservableList<BetWithName> getBets(int g_ID){
        ObservableList<BetWithName> ol = FXCollections.observableArrayList();

        String sql = "SELECT Player.name, Bet.numbers, Bet.remaining, Bet.paid, Bet.bet_ID "
                + "FROM Bet "
                + "INNER JOIN Player ON Bet.player_ID = Player.player_ID "
                + "WHERE game_ID = ?";

        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, g_ID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String aux = "";
                if (rs.getString("paid").equals("1"))
                    aux = "OK";
                ol.add(new BetWithName(rs.getString("name"), rs.getString("numbers"), rs.getString("remaining"), aux, Integer.parseInt(rs.getString("bet_ID"))));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ol;
    }

    public String getName() {
        return name;
    }

    public String getNumbers() {
        return numbers;
    }

    public String getRemaining() {
        return remaining;
    }

    public String getPaid() {
        return paid;
    }

    public int getBet_ID() {
        return bet_ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public void setBet_ID(int bet_ID) {
        this.bet_ID = bet_ID;
    }
}
