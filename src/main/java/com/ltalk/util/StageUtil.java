package com.ltalk.util;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
                Platform.exit();
            });
        }else{
            closeButton.setOnAction(event -> {
                stage.close();
            });
        }
        closeButton.setCursor(Cursor.HAND);
    }

    public void hideButton(Button hideButton, AnchorPane acp) {
        hideButton.setOnAction(event -> {Stage stage = (Stage) acp.getScene().getWindow(); stage.setIconified(true);});
    }


    private void stageMove(AnchorPane acp, Stage stage) {
        if(stage != null){
            acp.setOnMousePressed((event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            acp.setOnMouseDragged((event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
        }
    }


}
