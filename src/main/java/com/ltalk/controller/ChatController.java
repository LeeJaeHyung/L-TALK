package com.ltalk.controller;

import com.ltalk.dto.ChatDTO;
import com.ltalk.dto.ChatRoomDTO;
import com.ltalk.service.ChatService;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.ltalk.controller.MainController.member;

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

    @Getter
    @Setter
    private Boolean isOpen = false;

    private final DateTimeFormatter chatFormatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREA);
    private final DateTimeFormatter userDataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm", Locale.KOREA);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public ChatController (ChatRoomDTO chatRoomdto) {
        this.chatRoomdto = chatRoomdto;
    }

    public void init(Stage stage){
        setStage(stage);
        System.out.println("ChatController 초기화 진행 ");
        StageUtil stageUtil = new StageUtil();
        stageUtil.importChatBasicEvent(acp, stage, closeButton, this);
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
        chatBox.getChildren().clear();
        HBox beforeChatBox = null;
        List<ChatDTO> chatDTOs = chatRoomdto.getChats();
        synchronized (chatDTOs){
            for(ChatDTO chatDTO : chatDTOs){
                HBox positionBox = null;
                if(beforeChatBox != null){//이전 메세지가 있음
                    ChatDTO beforeChatDTO = (ChatDTO) beforeChatBox.getUserData();
                    if(beforeChatDTO.getSender().equals(chatDTO.getSender())){//작성자가 같은가?
                        if(beforeChatDTO.getUnreadCount()==chatDTO.getUnreadCount() &&
                                beforeChatDTO.getCreatedAt().format(userDataFormatter).equals(chatDTO.getCreatedAt().format(userDataFormatter))){//시간이 같음
                            positionBox = editChat(false, beforeChatBox, chatDTO);
                        }else{// 읽은사람수와 시간이 다름
                            positionBox = editChat(true, beforeChatBox, chatDTO);
                        }
                    }else{// 작성자가 다름
                        positionBox = makeNewChat(chatDTO.getSender().equals(member.getUsername()), chatDTO);
                        chatBox.getChildren().add(positionBox);
                    }
                }else{//이전 메세지가 없음
                    positionBox = makeNewChat(chatDTO.getSender().equals(member.getUsername()), chatDTO);
                    chatBox.getChildren().add(positionBox);
                }
                positionBox.setUserData(chatDTO);
                beforeChatBox = positionBox;
            }
        }
        Platform.runLater(()->{
            scrollPane.setVvalue(1.0);
        });
    }

    private void send() throws IOException {
        String message = textArea.getText();
        ChatService chatService = new ChatService();
        chatService.chat(chatRoomdto.getId(), MainController.getMember().getId(),message);
        addChat(message);
    }

    private void addChat(String message) throws IOException {
        Platform.runLater(() -> {
            textArea.clear();
        });
    }

    public void newChat(){
        if(isOpen){
            Platform.runLater(() -> { // UI 변경을 JavaFX UI 쓰레드에서 실행
                initChatBox();
            });
        }
    }

    public Label makeMessage(ChatDTO chatDTO, boolean isMyChat){
        Label message = new Label(chatDTO.getMessage());
        if(isMyChat){
            message.setStyle("-fx-background-color: yellow;");
        }else{
            message.setStyle("-fx-background-color: white;");
        }
        return message;
    }

    public HBox makeNewChat(boolean isMyChat, ChatDTO chatDTO){
        Label message = makeMessage(chatDTO, isMyChat);
        VBox messageBox = new VBox();
        HBox imageBox = new HBox();
        HBox chatPackage = new HBox();
        HBox messageTimeCountBox = new HBox();
        VBox messagePackage = new VBox();
        VBox timeBox = new VBox();
        Text time = new Text(chatFormatter.format(chatDTO.getCreatedAt()));
        Text unReadCount = new Text(Integer.toString(chatDTO.getUnreadCount()));
        timeBox.getChildren().addAll(time, unReadCount);
        messageBox.getChildren().add(message);
        if(isMyChat){
            chatPackage.setAlignment(Pos.TOP_RIGHT);
        }else{
            messagePackage.getChildren().add(new Text(chatDTO.getSender()));
            chatPackage.setAlignment(Pos.TOP_LEFT);
            ImageView imageView = new ImageView("/images/friend.png");
            imageBox.getChildren().add(imageView);
        }
        messageTimeCountBox.getChildren().addAll(messageBox, timeBox);
        messagePackage.getChildren().add(messageTimeCountBox);
        timeBox.setAlignment(Pos.BOTTOM_RIGHT);
        imageBox.setAlignment(Pos.TOP_CENTER);
        chatPackage.getChildren().addAll(imageBox,messagePackage);
        messagePackage.setStyle("-fx-border-color: red;");
        messageBox.setStyle("-fx-border-color: blue;");
        return chatPackage;
    }

    public HBox editChat(boolean setTime, HBox chatPackage, ChatDTO chatDTO){
        VBox messagePackage = (VBox) chatPackage.getChildren().get(chatPackage.getChildren().size() - 1);
        Label message = makeMessage(chatDTO, chatDTO.getSender().equals(member.getUsername()));
        HBox beforeMessageTimeCountBox = (HBox) messagePackage.getChildren().get(messagePackage.getChildren().size()-1);
        if (setTime){
            VBox messageBox = new VBox();
            VBox timeBox = new VBox();
            messageBox.getChildren().add(message);
            timeBox.getChildren().addAll(new Text(chatDTO.getCreatedAt().format(chatFormatter)), new Text(Integer.toString(chatDTO.getUnreadCount())));
            HBox messageTimeCountBox = new HBox();
            messageTimeCountBox.getChildren().addAll(messageBox, timeBox);
            messagePackage.getChildren().add(messageTimeCountBox);
            timeBox.setAlignment(Pos.BOTTOM_RIGHT);
        }else{
            VBox messageBox = (VBox)beforeMessageTimeCountBox.getChildren().get(0);
            messageBox.getChildren().add(message);
        }
        return chatPackage;
    }

}
