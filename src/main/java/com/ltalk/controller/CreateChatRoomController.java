package com.ltalk.controller;

import com.ltalk.dto.FriendDTO;
import com.ltalk.service.ChatService;
import com.ltalk.util.StageUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateChatRoomController implements Initializable {
    @FXML
    public VBox scrVBox;
    @FXML
    public Button closeBtn;
    @FXML
    public AnchorPane acp;

    public static CreateChatRoomController createChatRoomController;
    public static boolean createChatRoomIsOpened = false;
    public ChatService chatService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createChatRoomController = this;
        createChatRoomIsOpened = true;
        acp.setPickOnBounds(true);
        acp.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;  // ✅ 여기서 Stage 안전하게 확보됨
                        StageUtil stageUtil = new StageUtil();
                        stageUtil.importChatBasicEvent(acp, stage, closeBtn);
                    }
                });
            }
        });
        setFriendFiled();
    }

    public void setFriendFiled(){
        List<FriendDTO> friendDTOList =  MainController.getFriendList();
        for (FriendDTO friendDTO : friendDTOList) {
            Label label = new Label(friendDTO.getFriendName()+friendDTO.getFriendId()+friendDTO.getStatus());
            scrVBox.getChildren().add(label);
            label.setUserData(friendDTO);
            label.setOnMouseClicked((event) -> {
                Label clickedLabel = (Label) event.getSource();
                FriendDTO friendDTO1 = (FriendDTO) clickedLabel.getUserData();
                try {
                    chatService = new ChatService();
                    chatService.creatRoom(friendDTO1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }



}
