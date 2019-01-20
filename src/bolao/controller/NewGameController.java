package bolao.controller;

import bolao.ConnectionFactory;
import bolao.Manager;
import bolao.model.Bet;
import bolao.model.Game;
import bolao.model.ReportsGenerator;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewGameController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TextField gameSizeTF;
    @FXML private Button confirmBtn;
    @FXML private Label newGameMsg;

    private Manager application;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void registerGame(ActionEvent event) {
        int g_ID = 0;
        String aux = gameSizeTF.getText();
        List<Bet> bets = new ArrayList<Bet>();

        if(!"".equals(aux) && aux.matches("[0-9]+")){
            Game game = new Game(Integer.parseInt(gameSizeTF.getText()));
            game.registerNewGame();
            gameSizeTF.clear();

            String sql = "SELECT * FROM sqlite_sequence WHERE name = (?)";
            Connection conn;
            PreparedStatement ps;

            try {
                conn = ConnectionFactory.connect();
                ps = conn.prepareStatement(sql);
                ps.setString(1, "Game");
                ResultSet rs = ps.executeQuery();


                while (rs.next()){
                    g_ID = Integer.parseInt(rs.getString("seq"));
                    System.out.println(g_ID);
                }
                int antID = g_ID - 1;

                sql =   "SELECT * " +
                        "FROM Bet " +
                        "WHERE game_ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, antID);
                rs = ps.executeQuery();

                Bet auxBet;
                while (rs.next()){
                    auxBet = new Bet(rs.getInt("player_ID"), rs.getString("numbers"));
                    bets.add(auxBet);
                }

                sql =   "DELETE FROM Bet " +
                        "WHERE game_ID != ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, -1);
                ps.executeUpdate();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportsGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }

            for(Bet b: bets){
                b.registerNewBet();
            }
            newGameMsg.setText("Novo Jogo Iniciado");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Impossível cadastrar");
            alert.setContentText("Encerre o jogo atual para continuar.");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setApp(Manager application){
        this.application = application;
    }
}
