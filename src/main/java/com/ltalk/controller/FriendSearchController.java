package com.ltalk.controller;

import com.ltalk.dto.FriendDTO;
import com.ltalk.service.FriendService;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friendSearchController = this;
        friendSearchIsOpened = true;
        borderPane.setPickOnBounds(true);
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;  // ✅ 여기서 Stage 안전하게 확보됨
                        StageUtil stageUtil = new StageUtil();
                        stageUtil.importChatBasicEvent(borderPane, stage, closeBtn);
                    }
                });
            }
        });
        searchFriend();
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
                Label label = new Label(dto.getFriendName());
                scrPBox.getChildren().add(label);
                label.setUserData(dto);
                label.setOnMouseClicked(mouseEvent -> {
                    Label clickedLabel = (Label) mouseEvent.getSource();  // getSource() 사용!
                    friendService.requestFriend((FriendDTO) clickedLabel.getUserData());
                });

            }
        });
    }

}
