package com.ltalk.controller;

import com.ltalk.dto.FriendDTO;
import com.ltalk.enums.FriendStatus;
import com.ltalk.service.FriendService;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FriendSearchController implements Initializable {

    @FXML
    public BorderPane borderPane;

    @FXML
    public VBox scrPBox;

    @FXML
    Button searchBtn;

    @FXML
    Button closeBtn;

    @FXML
    TextField friendNameField;

    @FXML
    ScrollPane scrPane;

    private FriendService friendService = new FriendService();
    public static FriendSearchController friendSearchController;
    public static boolean friendSearchIsOpened = false;
    private Stage stage;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friendSearchController = this;
        friendSearchIsOpened = true;
        borderPane.setPickOnBounds(true);
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        stage = (Stage) newWindow;  // ✅ 여기서 Stage 안전하게 확보됨
                        StageUtil stageUtil = new StageUtil();
                        stageUtil.importChatBasicEvent(borderPane, stage, closeBtn);
                    }
                });
            }
        });
        searchFriend();
        scrPBox.setPadding(new Insets(10, 10, 10, 10));
    }

    public void searchFriend(){
        searchBtn.setOnMouseClicked(mouseEvent -> {
            String userName = friendNameField.getText();
            friendNameField.setText("");
            friendService.searchFriend(userName);
        });
    }

    public void setScrPane(List<FriendDTO> friendDTOList){
        Platform.runLater(() -> {
            scrPBox.getChildren().clear();
            for (FriendDTO dto : friendDTOList) {
                if(dto.getFriendName().equals(MainController.getMember().getUsername()))continue;
                if(checkAlreadyFriend(dto.getFriendName()))continue;
                HBox hBox = new HBox();
                Label label = new Label(dto.getFriendName());
                label.setStyle("-fx-font-weight: bold;" +
                        "-fx-background-color: white;" +
                        "-fx-pref-width: 300px;" +
                        "-fx-pref-height: 50px;" +
                        "-fx-alignment: center;"
                );
                scrPBox.getChildren().add(hBox);
                Button button = new Button("추가");
                button.setPrefHeight(50);
                HBox.setMargin(label,new Insets(5));
                hBox.getChildren().addAll(label,button);
                button.setUserData(dto);
                button.setOnMouseClicked(mouseEvent -> {
                    Button clickedButton = (Button) mouseEvent.getSource();  // getSource() 사용!
                    friendService.requestFriend((FriendDTO) clickedButton.getUserData());
                    Platform.runLater(()->{
                        stage.close();
                    });
                });

            }
        });
    }

    public boolean checkAlreadyFriend(String friendName){
        List<FriendDTO> fiendList = MainController.getFriendList();
        for (FriendDTO friendDTO : fiendList) {
            if (friendDTO.getFriendName().equals(friendName)&&friendDTO.getStatus()== FriendStatus.ACCEPTED) return true;
        }
        return false;
    }

}
