package bolao;

import bolao.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Manager extends Application {

    private Stage window;

    public static void main(String [] args){ Application.launch(Manager.class, (java.lang.String[]) null); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Principal");

        goLoginScreen();

        primaryStage.show();
    }

    //METODOS PARA ATUALIZAR A TELA
    public void goLoginScreen(){
        LoginScreenController screen = (LoginScreenController) replaceSceneContent("view/loginScreenFXML.fxml", "Fazer login");
        screen.setApp(this);
    }

    public void goMainScreen()  {
        HomeController screen = (HomeController) replaceSceneContent("view/homeFXML.fxml", "Principal");
        screen.setApp(this);
    }

    public void goNewGameScreen()  {
        NewGameController screen = (NewGameController) replaceSceneContent("view/newGameFXML.fxml", "Cadastrar Novo Jogo");
        screen.setApp(this);
    }

    public void goNewPlayerScreen(){
        NewPlayerController screen = (NewPlayerController) replaceSceneContent("view/newPlayerFXML.fxml", "Cadastrar novo jogador");
        screen.setApp(this);
    }

    public void goNewDrawScreen(){
        NewDrawController screen = (NewDrawController) replaceSceneContent("view/newDrawFXML.fxml", "Registrar novo sorteio");
        screen.setApp(this);
    }

    public void goNewBetScreen(){
        NewBetController screen = (NewBetController) replaceSceneContent("view/newBetFXML.fxml", "Registrar nova aposta");
        screen.setApp(this);
    }

    public void goEditPlayerScreen(){
        EditPlayerController screen = (EditPlayerController) replaceSceneContent("view/editPlayerFXML.fxml", "Editar jogador");
        screen.setApp(this);
    }

    public void goShowBetsScreen(){
        ShowBetsController screen = (ShowBetsController) replaceSceneContent("view/showBetsFXML.fxml", "Listar apostas");
        screen.setApp(this);
    }

    public void goEditUserScreen(){
        EditUserController screen = (EditUserController) replaceSceneContent("view/editUserFXML.fxml", "Editar usuarios");
        screen.setApp(this);
    }

    public void goNewUserScreen(){
        NewUserController screen = (NewUserController) replaceSceneContent("view/newUserFXML.fxml", "Adicionar Usuario");
        screen.setApp(this);
    }

    public void goNumbersEditBetScreen(){
        NumbersEditBetController screen = (NumbersEditBetController) replaceSceneContent("view/numbersEditBetFXML.fxml", "Editar aposta");
        screen.setApp(this);
    }


    //METODO QUE ATUALIZA A TELA DE ACORDO COM O ARGUMENTO PASSADO
    private Object replaceSceneContent(String fxml, String title) {
        AnchorPane page = null;
        Scene scene;
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Manager.class.getResourceAsStream(fxml);

        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Manager.class.getResource(fxml));


        try {
            page = (AnchorPane) loader.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        scene = new Scene(page);
        window.setScene(scene);
        window.sizeToScene();
        window.setTitle(title);
        return (Initializable) loader.getController();
    }


}
