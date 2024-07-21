package com.ltalk.controller;

import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public static Stage singUpStage;
    public Button submitButton;

    @FXML
    AnchorPane acp;
    @FXML
    Button closeButton;
    StageUtil stageutil = new StageUtil();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stageutil.importBasicsEvent(acp, singUpStage, closeButton);
        new SocketController();
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
        SocketController.socketController.signup();
    }
}
