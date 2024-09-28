package com.ltalk.controller;

import com.ltalk.util.StageUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    Stage stage;
    @FXML
    AnchorPane acp;
    @FXML
    Button closeButton;
    @FXML
    Button hideButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void init(Stage stage){
        setStage(stage);
        System.out.println("ChatController 초기화 진행 ");
        StageUtil stageUtil = new StageUtil();
        stageUtil.importBasicsEvent(acp, stage, closeButton, false);
        stageUtil.hideButton(hideButton, acp);
        System.out.println("초기화 완료 ");
        System.out.println("isNull"+(acp == null));
    }

    public void setStage(Stage stage){
        this.stage = stage;
        System.out.println("setStage() 실행 완료");
        System.out.println(stage==null);
    }

    public Stage getStage(){
        return stage;
    }



}
