package com.ltalk.controller;

import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public static Stage singUpStage;

    @FXML
    AnchorPane acp;
    @FXML
    Button closeButton;
    StageUtil stageutil = new StageUtil();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stageutil.importBasicsEvent(acp, singUpStage, closeButton);
    }

    public static void setStage(Stage stage){
        singUpStage = stage;
    }
}
