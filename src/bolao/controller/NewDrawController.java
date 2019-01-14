package bolao.controller;

import bolao.Manager;
import bolao.model.Draw;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewDrawController implements Initializable {
    @FXML private Button returnBtn;
    @FXML private TextField numbersDrawTF;
    @FXML private Button registerBtn;
    @FXML private Label newDrawMsg;


    private Manager application;

    @FXML void previousScreen(ActionEvent event) {
        this.application.goMainScreen();
    }

    @FXML void registerDraw(ActionEvent event) {
        String aux = numbersDrawTF.getText();
        if (!"".equals(aux) && aux.matches("[0-9 ]+[0-9]*")){
            Draw newDraw = new Draw(numbersDrawTF.getText());
            newDraw.registerNewDraw();
            newDrawMsg.setText("Novo Sorteio Cadastrado com Sucesso");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(Manager application){
        this.application = application;
    }
}
