package com.ltalk.controller;

import com.ltalk.util.StageUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public static Stage singUpStage;
    public Button submitButton;
    public TextField idField;
    public PasswordField passwordField;
    public TextField emailField;

    @FXML
    AnchorPane acp;
    @FXML
    Button closeButton;
    StageUtil stageutil = new StageUtil();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stageutil.importBasicsEvent(acp, singUpStage, closeButton);
        submitButton.setOnMouseClicked(mouseEvent -> {
            try{
                submit();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public static void setStage(Stage stage){
        singUpStage = stage;
    }

    public void submit() throws IOException {
        if(!idField.getText().isEmpty() && !passwordField.getText().isEmpty() && !emailField.getText().isEmpty()){
            SocketController.getInstance().signup(idField.getText(), passwordField.getText(), emailField.getText());
        }
    }

}
