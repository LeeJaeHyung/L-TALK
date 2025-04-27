package com.ltalk.controller;

import com.ltalk.dto.FriendDTO;
import com.ltalk.entity.Data;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.ChatRoomCreationCheckRequest;
import com.ltalk.service.ChatService;
import com.ltalk.util.StageUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.ltalk.controller.SocketController.sendData;

public class CreateChatRoomController implements Initializable {
    @FXML
    public VBox scrVBox;
    @FXML
    public Button closeBtn;
    @FXML
    public AnchorPane acp;
    public Stage stage;

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
                        Stage stage = (Stage) newWindow;
                        this.stage = stage;// ✅ 여기서 Stage 안전하게 확보됨
                        StageUtil stageUtil = new StageUtil();
                        stageUtil.importChatBasicEvent(acp, stage, closeBtn);
                    }
                });
            }
        });
        getCreationChatRoom();
    }

    public void setFriendFiled(List<FriendDTO> friendDTOList){
        for (FriendDTO friendDTO : friendDTOList) {
            Label label = new Label(friendDTO.getFriendName()+friendDTO.getFriendId()+friendDTO.getStatus());
            HBox hBox = new HBox();
            scrVBox.getChildren().add(hBox);
            Button button = new Button("생성");
            hBox.getChildren().addAll(label,button);
            button.setCursor(Cursor.HAND);
            button.setUserData(friendDTO);
            label.setStyle("-fx-font-weight: bold;" +
                    "-fx-background-color: white;" +
                    "-fx-pref-width: 300px;" +
                    "-fx-pref-height: 50px;" +
                    "-fx-alignment: center;"
            );
            button.setOnMouseClicked((event) -> {
                Button clickedLabel = (Button) event.getSource();
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
    public void getCreationChatRoom(){
        sendData(new Data(ProtocolType.CAN_CREATE_CHAT_ROOM,new ChatRoomCreationCheckRequest(MainController.member.getId())));
    }



}
