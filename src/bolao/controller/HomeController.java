package bolao.controller;

import bolao.ConnectionFactory;
import bolao.Manager;
import bolao.model.Checker;
import bolao.model.Game;
import bolao.model.ReportsGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private Label dateBgnLabel;
    @FXML private Label dateEndLabel;
    @FXML private Label numbersBetLabel;
    @FXML private Label numbersPlyrLabel;
    @FXML private Label numbersDrawLabel;
    @FXML private Label lastDrawLabel;
    @FXML private Label todayLabel;
    @FXML private Button setCloseDateBtn;
    @FXML private Button setEndGameBtn;
    @FXML private Button registerNewGameBtn;
    @FXML private Button registerNewPlayerBtn;
    @FXML private Button registerNewBetBtn;
    @FXML private Button registerNewDrawBtn;
    @FXML private Button betsReportBtn;
    @FXML private Button hitsReportBtn;
    @FXML private Button editPlayerBtn;
    @FXML private Button checkLastDrawBtn;
    @FXML private Button winnersReportBtn;
    @FXML private Button listBetsBtn;
    @FXML private Button reopenBetsBtn;
    @FXML private Button reopenGameBtn;
    @FXML private Button registerNewUser;
    @FXML private Button editUserBtn;
    @FXML private Button alphabeticBtn;



    private Manager application;
    int game_ID = 0, draw_ID = 0;


    @FXML void callNewBetScreen(ActionEvent event) {
        this.application.goNewBetScreen();
    }

    @FXML void callNewDrawScreen(ActionEvent event) {
        this.application.goNewDrawScreen();
    }

    @FXML void callNewGameScreen(ActionEvent event) {
        this.application.goNewGameScreen();
    }

    @FXML void callNewPlayerScreen(ActionEvent event) {
        this.application.goNewPlayerScreen();
    }

    @FXML void callEditPlayerScreen(ActionEvent event) {
        this.application.goEditPlayerScreen();
    }

    @FXML void callListBetsScreen(ActionEvent event) {
        this.application.goShowBetsScreen();
    }

    @FXML void closeDraws(ActionEvent event) {
        Game g = new Game(0);
        g.setFinal_Date(game_ID);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Encerramento de apostas");
        alert.setHeaderText("Fechamento de apostas");
        alert.setContentText("Apostas encerradas!");
        alert.showAndWait();
        reInitialize();
    }

    @FXML void closeGame(ActionEvent event) {
        Game g = new Game(0);
        g.setClose_Date(game_ID);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fechamento do jogo");
        alert.setHeaderText("Fechamento do jogo");
        alert.setContentText("Jogo finalizado com sucesso!");
        alert.showAndWait();

        reInitialize();
    }

    @FXML void generateBetsReport(ActionEvent event) {
        ReportsGenerator.betsReport(game_ID);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatorio");
        alert.setHeaderText("Relatorio gerado");
        alert.setContentText("Veja na pasta PDF o relatorio criado.");
        alert.showAndWait();
    }

    @FXML void generateHitsReport(ActionEvent event) {
        ReportsGenerator.hitsPerDrawReport(game_ID, draw_ID, 0);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatorio");
        alert.setHeaderText("Relatorio gerado");
        alert.setContentText("Veja na pasta PDF o relatorio criado.");
        alert.showAndWait();
    }

    @FXML void generateHitsReportsAlph(ActionEvent event) {
        ReportsGenerator.hitsPerDrawReport(game_ID, draw_ID, 1);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatorio");
        alert.setHeaderText("Relatorio gerado");
        alert.setContentText("Veja na pasta PDF o relatorio criado.");
        alert.showAndWait();
    }

    @FXML void checkLastDraw(ActionEvent event) {
        if (Checker.checkForWinner()){
            Game g = new Game(0);
            g.setFinal_Date(game_ID);
        }
    }

    @FXML void generateWinnersReport(ActionEvent event) {
        ReportsGenerator.winnersReport(game_ID, draw_ID);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatorio");
        alert.setHeaderText("Relatorio gerado");
        alert.setContentText("Veja na pasta PDF o relatorio criado.");
        alert.showAndWait();
    }

    @FXML void reopenBets(ActionEvent event) {
        Game g = new Game(0);
        g.reopenBets(game_ID);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Apostas");
        alert.setHeaderText("Apostas reabertas.");
        alert.setContentText("Periodo de apostas reaberto com sucesso.");
        alert.showAndWait();
        reInitialize();
    }

    @FXML void reopenGame(ActionEvent event) {
        Game g = new Game(0);
        g.reopenGame(game_ID);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Jogo");
        alert.setHeaderText("Jogo reaberto.");
        alert.setContentText("Jogo reaberto com sucesso.");
        alert.showAndWait();
        reInitialize();
    }

    @FXML void callNewUserScreen(ActionEvent event) {
        this.application.goNewUserScreen();
    }

    @FXML void callEditUserScreen(ActionEvent event) {
        this.application.goEditUserScreen();
    }


    private void reInitialize(){
        Connection conn;
        PreparedStatement ps = null;
        String sql;
        ResultSet rs;
        try {

            //SEARCH THE MOST RECENT GAME AND DRAW
            sql = "SELECT * FROM sqlite_sequence WHERE name = (?) OR name = (?)";

            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Game");
            ps.setString(2, "Draw");
            rs = ps.executeQuery();

            while(rs.next()){
                if(rs.getString("Name").equals("Game")){
                    game_ID = Integer.parseInt(rs.getString("seq"));
                } else if(rs.getString("Name").equals("Draw")){
                    draw_ID = Integer.parseInt(rs.getString("seq"));
                }
            }

            //SEARCH FOR THE GAME AND SETS THE LABELS FOR THE BEGIN AND END DATE
            sql = "SELECT * FROM Game WHERE game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            dateBgnLabel.setText(rs.getString("begin_Date"));
            dateEndLabel.setText(rs.getString("final_Date"));

            //GETS THE NUMBER OF BETS AND SETS THE LABEL
            sql = "SELECT count(*) AS total FROM Bet WHERE Game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            numbersBetLabel.setText(rs.getString("total"));

            //FINDS THE NUMBER OF PLAYERS PLAYING THE CURRENT GAME
            sql = "SELECT count (distinct player_ID) AS total FROM Bet WHERE Game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            numbersPlyrLabel.setText(rs.getString("total"));

            //COUNTS THE NUMBER OF DRAWS FOR THE MOST RECENT GAME UPDATE LABEL
            sql = "SELECT count(*) as total FROM Draw WHERE game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            numbersDrawLabel.setText(rs.getString("total"));

            //GETS THE NUMBERS OF THE MOST RECENT DRAW AND UPDATE LABEL
            sql = "SELECT * FROM Draw WHERE draw_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, draw_ID);
            rs = ps.executeQuery();
            lastDrawLabel.setText(rs.getString("numbers"));

            conn.close();

        } catch(SQLException e){
        }

        LocalDate ld = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
        todayLabel.setText(ld.format(formatter));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection conn;
        PreparedStatement ps = null;
        String sql;
        ResultSet rs;
        try {

            //SEARCH THE MOST RECENT GAME AND DRAW
            sql = "SELECT * FROM sqlite_sequence WHERE name = (?) OR name = (?)";

            conn = ConnectionFactory.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Game");
            ps.setString(2, "Draw");
            rs = ps.executeQuery();

            while(rs.next()){
                if(rs.getString("Name").equals("Game")){
                    game_ID = Integer.parseInt(rs.getString("seq"));
                } else if(rs.getString("Name").equals("Draw")){
                    draw_ID = Integer.parseInt(rs.getString("seq"));
                }
            }

            //SEARCH FOR THE GAME AND SETS THE LABELS FOR THE BEGIN AND END DATE
            sql = "SELECT * FROM Game WHERE game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            dateBgnLabel.setText(rs.getString("begin_Date"));
            dateEndLabel.setText(rs.getString("final_Date"));

            //GETS THE NUMBER OF BETS AND SETS THE LABEL
            sql = "SELECT count(*) AS total FROM Bet WHERE Game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            numbersBetLabel.setText(rs.getString("total"));

            //FINDS THE NUMBER OF PLAYERS PLAYING THE CURRENT GAME
            sql = "SELECT count (distinct player_ID) AS total FROM Bet WHERE Game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            numbersPlyrLabel.setText(rs.getString("total"));

            //COUNTS THE NUMBER OF DRAWS FOR THE MOST RECENT GAME UPDATE LABEL
            sql = "SELECT count(*) as total FROM Draw WHERE game_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, game_ID);
            rs = ps.executeQuery();
            numbersDrawLabel.setText(rs.getString("total"));

            //GETS THE NUMBERS OF THE MOST RECENT DRAW AND UPDATE LABEL
            sql = "SELECT * FROM Draw WHERE draw_ID = (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, draw_ID);
            rs = ps.executeQuery();
            lastDrawLabel.setText(rs.getString("numbers"));

            conn.close();

        } catch(SQLException e){
        }

        LocalDate ld = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
        todayLabel.setText(ld.format(formatter));
    }

    public void setApp(Manager application){
        this.application = application;
    }

}


