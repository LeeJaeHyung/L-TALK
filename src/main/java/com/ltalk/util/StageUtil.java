package com.ltalk.util;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicReference;

public class StageUtil {

    double x,y;

    public static void setStageUtil(Stage stage){
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/images/LTalkIcon.png"));
    }

    public void importBasicsEvent(AnchorPane acp, Stage stage, Button closeButton){
        stageMove(acp, stage);
        closeButton.setOnAction(event -> {
            stage.close();
        });
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
