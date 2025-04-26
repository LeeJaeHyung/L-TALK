package com.ltalk.util;

import com.ltalk.controller.ChatController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.ltalk.controller.FriendSearchController.friendSearchIsOpened;
import static com.ltalk.controller.SocketController.disconnect;

public class StageUtil {

    double x,y;

    public static void setStageUtil(Stage stage){
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/images/LTalkIcon.png"));
    }

    public void importChatBasicEvent(AnchorPane acp, Stage stage, Button closeButton, ChatController chatController){
        stageMove(acp, stage);
        closeButton.setOnAction(event -> {
            chatController.setIsOpen(false);
            stage.close();
        });
    }

    public void importChatBasicEvent(Node node, Stage stage, Button closeButton){
        stageMove(node,stage);
        closeBtnEvent(closeButton, node);
    }

    public void closeBtnEvent(Button closeButton, Node node){
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
            friendSearchIsOpened = false;
        });

    }

    public void importBasicsEvent(Node acp, Stage stage, Button closeButton, boolean isMain){
        stageMove(acp, stage);
        if(isMain){
            closeButton.setOnAction(event -> {
                try {
                    stage.close();
                    Platform.exit();
                } catch (Exception e) {
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

    public void stageMove(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        System.out.println(stage==null);
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
