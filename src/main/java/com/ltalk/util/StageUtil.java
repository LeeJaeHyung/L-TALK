package com.ltalk.util;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.ltalk.controller.SocketController.disconnect;

public class StageUtil {

    double x,y;

    public static void setStageUtil(Stage stage){
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/images/LTalkIcon.png"));
    }

    public void importBasicsEvent(AnchorPane acp, Stage stage, Button closeButton, boolean isMain){
        stageMove(acp, stage);
        if(isMain){
            closeButton.setOnAction(event -> {
                try {
                    disconnect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }else{
            closeButton.setOnAction(event -> {
                stage.close();
            });
        }
        System.out.println("stageClose적용완료");
        closeButton.setCursor(Cursor.HAND);
    }

    public void hideButton(Button hideButton, AnchorPane acp) {
        hideButton.setOnAction(event -> {Stage stage = (Stage) acp.getScene().getWindow(); stage.setIconified(true);});
    }


    public void stageMove(Node node, Stage stage) {
        if(stage != null){
            node.setOnMousePressed((event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            node.setOnMouseDragged((event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
        }
    }



}
