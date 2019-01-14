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
public class Game {

    private String begin_Date;
    private String final_Date;
    private String close_Date;
    private int game_Size;

    //CONSTRUCTOR
    public Game(int game_size){
        setBegin_Date();
        setGame_Size(game_size);
    }

    //GETTERS
    public String getBegin_Date() {

        return begin_Date;
    }

    public String getFinal_Date(int game_ID) {
        String sql = "SELECT final_Date FROM Game WHERE game_ID = (?)";
        String dateAux = "";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                dateAux = rs.getString("final_Date");
            }
            conn.close();
            if(dateAux.equals("null")){
                //JOGO AINDA EM ABERTO
            }
            else
                return dateAux;
        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public String getClose_Date(int game_ID) {
        String sql = "SELECT close_Date FROM Game WHERE game_ID = (?)";
        String dateAux = "";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                dateAux = rs.getString("close_Date");
            }
            conn.close();
            if(dateAux.equals("null")){
                //JOGO AINDA EM ABERTO
            }
            else
                return dateAux;
        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public int getGame_Size(int game_ID) {
        String sql = "SELECT * FROM Game WHERE game_ID = (?)";
        int i = 0, aux = 0;
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                aux = rs.getInt("game_ID");
            }
            conn.close();
            if(aux != 0)
                return aux;
            else{
                //JOGO INEXISTENTE
            }
        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return game_Size;
    }

    //SETTERS
    public void setBegin_Date() {
        LocalDate ld = LocalDate.now();
        String date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
        date = ld.format(formatter);
        this.begin_Date = date;
    }

    public void setFinal_Date (int game_ID) {
        LocalDate ld = LocalDate.now();
        String date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
        date = ld.format(formatter);
        String sql = "UPDATE Game SET (final_Date) = (?) WHERE game_ID = (?)";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setInt(2, game_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setClose_Date(int game_ID) {
        LocalDate ld = LocalDate.now();
        String date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
        date = ld.format(formatter);
        String sql = "UPDATE Game SET (close_Date) = (?) WHERE game_ID = (?)";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setInt(2, game_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGame_Size(int game_Size) {
        this.game_Size = game_Size;
    }

    //ADD TO DATTABASE
    public void registerNewGame(){
        String sql = "INSERT INTO Game (begin_Date,final_Date, close_Date, game_Size) VALUES (?, ?, ?, ?)";
        try {
            Connection conn = ConnectionFactory.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.begin_Date);
            ps.setString(2, "");
            ps.setString(3, "");
            ps.setInt(4, this.game_Size);
            ps.executeUpdate();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reopenBets(int g_ID){
        String sql = "UPDATE Game SET final_Date = ? WHERE game_ID = ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "");
            ps.setInt(2, g_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reopenGame(int g_ID){
        String sql = "UPDATE Game SET close_Date = ? WHERE game_ID = ?";
        Connection conn = ConnectionFactory.connect();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "");
            ps.setInt(2, g_ID);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}