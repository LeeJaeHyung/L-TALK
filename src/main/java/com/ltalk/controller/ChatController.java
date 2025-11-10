package com.ltalk.controller;

import com.ltalk.dto.ChatDTO;
import com.ltalk.dto.ChatRoomDTO;
import com.ltalk.service.ChatService;
import com.ltalk.service.VoiceService;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import java.time.LocalDateTime;
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

    @FXML
    VBox titleBox;

    @FXML
    Stage stage;
    @FXML
    AnchorPane acp;
    @FXML
    Button closeButton;
    @FXML
    Button hideButton;
    @FXML
    VBox chatBox;
    @FXML
    Button joinVoiceChat;

    @Getter
    @Setter
    private ChatRoomDTO chatRoomdto;

    @Getter
    @Setter
    private Boolean isOpen = false;

    private final DateTimeFormatter chatFormatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREA);
    private final DateTimeFormatter userDataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm", Locale.KOREA);
    private final DateTimeFormatter dateLabelFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE", Locale.KOREAN);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatBox.setStyle("-fx-background-color: #BACEE0");
        acp.setStyle("-fx-background-color: #BACEE0");

    }

    public ChatController (ChatRoomDTO chatRoomdto) {
        this.chatRoomdto = chatRoomdto;
    }

    public void init(Stage stage){
        setStage(stage);
        System.out.println("ChatController 초기화 진행 ");
        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.getChildren().addAll(new Text(chatRoomdto.getName()), new Text(chatRoomdto.getParticipantCount().toString()));
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
        joinVoiceChat.setOnMouseClicked(event -> {
            joinVoiceChat(chatRoomdto.getId());
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
                    if(!dateCompare(beforeChatDTO.getCreatedAt(), chatDTO.getCreatedAt())){// 이전 채팅과 날짜가 다른가?
                        positionBox = makeNewChat(chatDTO.getSender().equals(member.getUsername()), chatDTO);
                        chatBox.getChildren().add(positionBox);
                    } else if(beforeChatDTO.getSender().equals(chatDTO.getSender())){//작성자가 같은가?
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
        if(!message.equals("")){
            ChatService chatService = new ChatService();
            chatService.chat(chatRoomdto.getId(), MainController.getMember().getId(),message);
            addChat(message);
        }
        Platform.runLater(() -> {
            textArea.clear();
        });
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

    public boolean dateCompare(LocalDateTime beforeDate, LocalDateTime date){
        boolean isEquals = beforeDate.toLocalDate().equals(date.toLocalDate());
        if (!isEquals){
            VBox dateBox = makeDateLabel(date);
            dateBox.setAlignment(Pos.CENTER);
            chatBox.getChildren().add(dateBox);
        }
        return isEquals;
    }

    public VBox makeDateLabel(LocalDateTime date){
        VBox dateBox = new VBox();
        Label dateLabel = new Label(dateLabelFormatter.format(date));
        dateBox.getChildren().add(dateLabel);
        return dateBox;
    }

    public Label makeMessage(ChatDTO chatDTO, boolean isMyChat){
        Label message = new Label(chatDTO.getMessage());
        if(isMyChat){
            message.setStyle("-fx-background-color: yellow;" +
                    "    -fx-background-radius: 5; /* 둥글게! */\n" +
                    "    -fx-padding: 5 10 5 10; ");
        }else{
            message.setStyle("-fx-background-color: white;" +
                    "    -fx-background-radius: 5; /* 둥글게! */\n" +
                    "    -fx-padding: 5 10 5 10; ");
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
        messagePackage.setPadding(new Insets(8));
        messageTimeCountBox.setPadding(new Insets(3));
        VBox timeBox = new VBox();
        Text time = new Text(chatFormatter.format(chatDTO.getCreatedAt()));
        Text unReadCount = new Text(Integer.toString(chatDTO.getUnreadCount()));
        timeBox.getChildren().addAll(unReadCount, time);
        messageBox.getChildren().add(message);
        if(isMyChat){
            chatPackage.setAlignment(Pos.TOP_RIGHT);
            timeBox.setAlignment(Pos.BOTTOM_RIGHT);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageTimeCountBox.getChildren().addAll(timeBox, messageBox);
            messageTimeCountBox.setAlignment(Pos.CENTER_RIGHT);
        }else{
            messagePackage.getChildren().add(new Text(chatDTO.getSender()));
            chatPackage.setAlignment(Pos.TOP_LEFT);
            timeBox.setAlignment(Pos.BOTTOM_LEFT);
            messageBox.setAlignment(Pos.CENTER_LEFT);
            ImageView imageView = new ImageView("/images/friend.png");
            imageBox.getChildren().add(imageView);
            messageTimeCountBox.getChildren().addAll(messageBox, timeBox);
        }
        messagePackage.getChildren().add(messageTimeCountBox);
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
        boolean isMyChat = chatDTO.getSender().equals(member.getUsername());
        VBox messageBox = new VBox();
        VBox timeBox = new VBox();
        messageBox.getChildren().add(message);
        HBox messageTimeCountBox = new HBox();
        messageTimeCountBox.setPadding(new Insets(3));
        int timeIdx;
        if(isMyChat){
            timeIdx = 0;
            messageTimeCountBox.getChildren().addAll(timeBox, messageBox);
            timeBox.setAlignment(Pos.BOTTOM_RIGHT);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageTimeCountBox.setAlignment(Pos.CENTER_RIGHT);
        }else {
            timeIdx = 1;
            messageTimeCountBox.getChildren().addAll(messageBox, timeBox);
            timeBox.setAlignment(Pos.BOTTOM_LEFT);
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageTimeCountBox.setAlignment(Pos.CENTER_LEFT);
        }
        if(!setTime){
            VBox beforeTimeBox = (VBox)beforeMessageTimeCountBox.getChildren().get(timeIdx);
            beforeTimeBox.getChildren().clear();
        }
        timeBox.getChildren().addAll(new Text(Integer.toString(chatDTO.getUnreadCount())), new Text(chatDTO.getCreatedAt().format(chatFormatter)));

        messagePackage.getChildren().add(messageTimeCountBox);

        return chatPackage;
    }

    public void joinVoiceChat(Long chatRoomId){
        new VoiceService().getVoiceServerIP(chatRoomId);
    }

}
