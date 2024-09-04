package com.ltalk.controller;

import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
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
import java.util.ResourceBundle;
import static com.ltalk.util.StageUtil.setStageUtil;

@Getter
@Setter
public class MainController implements Initializable {
    @Getter
    @Setter
    private static Stage stage;
    @Getter
    @Setter
    private static Member member;
    @Getter
    @Setter
    private static List<Friend> friendList;

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

    private VBox friendBox = new VBox();
    private VBox chatBox = new VBox();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(member.getUsername());
        for(Friend friend : friendList) {
            System.out.println(friend.getFriend_name());
        }
        StageUtil stageUtil = new StageUtil();
        stageUtil.importBasicsEvent(acp,stage,closeButton,true);
        stageUtil.stageMove(hBox, stage);
        stageUtil.hideButton(hideButton,acp);
        initVBox();
        setBox(friendBox);
    }

    private void setBox(VBox vBox) {
        scrollPane.setContent(null);
        scrollPane.setContent(vBox);
    }

    private void initVBox() {
       initChatBox();
       initFriendBox();
    }
    private void initFriendBox() {
        friendBox.setStyle("-fx-background-color: #ffffff");
        ObservableList children = friendBox.getChildren();
        Text text = new Text("친구");
        text.setFont(new Font(20));
        children.add(text);
        for (Friend friend : friendList) {
            HBox box = new HBox();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setSpacing(5);
            box.setUserData(friend.getFriend_name());
            box.setPrefWidth(289);
            box.setAlignment(Pos.CENTER_LEFT);
            box.setSpacing(8);
            Rectangle rec = new Rectangle(40,40);
            rec.setArcHeight(25);
            rec.setArcWidth(25);
            Image im = new Image("file:src/main/resources/images/talk.png");
            rec.setFill(new ImagePattern(im));
            Label label = new Label(friend.getFriend_name());
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
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat-view.fxml"));
                    Scene popupScene;
                    Stage popup = new Stage();
                    SignUpController.setStage(popup);
                    try {
                        popupScene = new Scene(fxmlLoader.load(), 400, 600);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    popup.setTitle("L-Talk 회원가입");
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
