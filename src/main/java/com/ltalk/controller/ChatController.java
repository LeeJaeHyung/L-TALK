package com.ltalk.controller;

import com.ltalk.dto.ChatDTO;
import com.ltalk.dto.ChatRoomDTO;
import com.ltalk.entity.ChatRoom;
import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    public ScrollPane scrollPane;

    @FXML
    private TextArea textArea;

    @FXML
    private Button sendButton;

    Stage stage;
    @FXML
    AnchorPane acp;
    @FXML
    Button closeButton;
    @FXML
    Button hideButton;
    @FXML
    VBox chatBox;

    @Getter
    @Setter
    private ChatRoomDTO chatRoomdto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void init(Stage stage){
        setStage(stage);
        System.out.println("ChatController 초기화 진행 ");
        StageUtil stageUtil = new StageUtil();
        stageUtil.importBasicsEvent(acp, stage, closeButton, false);
        stageUtil.hideButton(hideButton, acp);
        initChatBox();
        System.out.println("초기화 완료 ");
        System.out.println("isNull"+(acp == null));
        sendButton.setOnMouseClicked(event -> {
            try {
                send();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            if (event.getCode() == KeyCode.ENTER){
                try {
                    send();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
        System.out.println("setStage() 실행 완료");
        System.out.println(stage==null);
    }

    public Stage getStage(){
        return stage;
    }

    private void initChatBox(){
        List<ChatDTO> chatDTOs = chatRoomdto.getChats();

        for(ChatDTO chatDTO : chatDTOs){
            Text text = new Text(""+chatDTO.getChatId()+chatDTO.getSender()+chatDTO.getMessage()+chatDTO.getCreatedAt());
            chatBox.getChildren().add(text);
        }

    }

    private void send() throws IOException {
        String message = textArea.getText();
        SocketController.getInstance().chat(chatRoomdto.getId(), MainController.getMember().getId(),message);
        Platform.runLater(() -> {
            textArea.clear();
            Label label = new Label(message);
            chatBox.getChildren().add(label);
        });
    }



}
