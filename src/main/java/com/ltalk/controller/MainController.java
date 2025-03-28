package com.ltalk.controller;

import com.ltalk.dto.ChatDTO;
import com.ltalk.dto.ChatRoomDTO;
import com.ltalk.dto.FriendDTO;
import com.ltalk.dto.MemberDTO;
import com.ltalk.entity.Chat;
import com.ltalk.enums.ViewBoxEnum;
import com.ltalk.service.ChatService;
import com.ltalk.service.FriendService;
import com.ltalk.util.StageUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import static com.ltalk.util.StageUtil.setStageUtil;

@Getter
@Setter
public class MainController implements Initializable {
    @Getter
    @Setter
    private static Stage stage;
    @Getter
    @Setter
    public static MemberDTO member;
    @Getter
    @Setter
    private static List<FriendDTO> friendList;
    @Getter
    @Setter
    public static List<ChatRoomDTO> chatRoomList;
    public static Map<Long, ChatController> chatControllerMap = new ConcurrentHashMap<>();
    private ViewBoxEnum viewBoxEnum;


    @FXML
    private Button friendButton;
    @FXML
    private Button chatButton;
    @FXML
    private AnchorPane acp;
    @FXML
    private HBox hBox;
    @FXML
    private Button closeButton;
    @FXML
    private Button hideButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox viewVBox;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button addChatRoomButton;

    private VBox friendBox = new VBox();
    private VBox chatBox = new VBox();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(member.getUsername());
        for(FriendDTO friend : friendList) {
            System.out.println(friend);
        }
        StageUtil stageUtil = new StageUtil();
        stageUtil.importBasicsEvent(acp,stage,closeButton,true);
        stageUtil.stageMove(hBox, stage);
        stageUtil.hideButton(hideButton,acp);
        chatButtonEvent();
        friendButtonEvent();
        addChatRoomEvent();
        try {
            initVBox();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addFriendButtonEvent();
        setBox(friendBox);
        viewBoxEnum = ViewBoxEnum.FRIEND;
    }

    private void friendButtonEvent(){
        System.out.println("friendButton click");
        friendButton.setCursor(Cursor.HAND);
        friendButton.setOnMouseClicked(event -> {
            System.out.println("friend button clicked");
            if(viewBoxEnum != ViewBoxEnum.FRIEND){
                viewVBox = friendBox;
                viewBoxEnum = ViewBoxEnum.FRIEND;
            }
        });
    }

    private void addFriendButtonEvent(){
        addFriendButton.setCursor(Cursor.HAND);
        addFriendButton.setOnMouseClicked(event -> {
            FriendService friendService = new FriendService();
            friendService.addFriend();
        });
    }

    private void addChatRoomEvent(){
        addChatRoomButton.setCursor(Cursor.HAND);
        addChatRoomButton.setOnMouseClicked(event -> {
            try {
                ChatService chatService = new ChatService();
                chatService.creatRoom();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void chatButtonEvent(){
        chatButton.setCursor(Cursor.HAND);
        chatButton.setOnMouseClicked(event -> {
            System.out.println("chat button clicked");
            if(viewBoxEnum != ViewBoxEnum.CHAT){
                viewVBox = chatBox;
                viewBoxEnum = ViewBoxEnum.CHAT;
            }
        });
    }

    private void setBox(VBox vBox) {
        scrollPane.setContent(null);
        scrollPane.setContent(vBox);
    }

    private void initVBox() throws IOException {
       initChatBox();
       initFriendBox();
    }

    private void initFriendBox() throws IOException {
        friendBox.setStyle("-fx-background-color: #ffffff");
        ObservableList children = friendBox.getChildren();
        Text text = new Text("채팅방");
        text.setFont(new Font(20));
        children.add(text);
        for (ChatRoomDTO chatRoom : chatRoomList) {
            ChatService chatService = new ChatService();
            chatService.readCount(chatRoom);
            ChatController controller = new ChatController(chatRoom);
            chatControllerMap.put(chatRoom.getId(), controller);
            HBox box = new HBox();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setSpacing(5);
            box.setUserData(chatRoom);
            box.setPrefWidth(289);
            box.setAlignment(Pos.CENTER_LEFT);
            box.setSpacing(8);
            Rectangle rec = new Rectangle(40,40);
            rec.setArcHeight(25);
            rec.setArcWidth(25);
            Image im = new Image("file:src/main/resources/images/talk.png");
            rec.setFill(new ImagePattern(im));
            Label label = new Label(chatRoom.getName());
            label.setStyle("-fx-background-color: transparent; -fx-font-family: 'Malgun Gothic Bold'; -fx-text-fill: #000000;");
            rec.setCursor(Cursor.HAND);
            rec.setOnMouseClicked(event -> {
                System.out.println("프로필 클릭 데스요~");
            });
            vBox.getChildren().add(label);
            box.getChildren().addAll(rec,vBox);
            box.setMargin(rec, new Insets(15,0,10,10));
            children.add(box);
            box.setOnMouseEntered(mouseEvent -> {
                box.setStyle("-fx-background-color: #ECECED;");
            });
            box.setOnMouseExited(mouseEvent -> {
                box.setStyle("-fx-background-color: #ffffff;");
            });
            box.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    try {
                        ChatService service = new ChatService();
                        List<ChatDTO> chatDTOList = controller.getChatRoomdto().getChats();
                        synchronized (chatDTOList){
                            service.readChat(chatRoom.getId(), chatDTOList.get(chatDTOList.size()-1).getChatId(), true);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat-view.fxml"));
                    fxmlLoader.setController(controller);
                    controller.setIsOpen(true);
                    Scene popupScene;
                    Stage popup = new Stage();
                    try {
                        popupScene = new Scene(fxmlLoader.load(), 400, 600);
                        controller.init(popup);
                        System.out.println(chatRoom==null);
                        System.out.println(chatRoom);
                        System.out.println("setChatroom ");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    popup.setTitle(""+box.getUserData());
                    popup.initOwner(stage); // 소유자 창 설정
                    setStageUtil(popup);
                    popup.setScene(popupScene);
                    popup.initModality(javafx.stage.Modality.APPLICATION_MODAL); // 모달 창 설정
                    popup.show();
                }
            });
        }
    }

    private void initChatBox(){
        chatBox.setStyle("-fx-background-color: #ffffff");
    }
}
