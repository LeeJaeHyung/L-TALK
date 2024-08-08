package com.ltalk.controller;

import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.ltalk.util.StageUtil.setStageUtil;

public class LTalkController  implements Initializable {

    @Setter
    private static Stage primaryStage = null;

    @FXML
    AnchorPane acp;
    @FXML
    Button loginButton;
    @FXML
    Button closeButton;
    @FXML
    Button hideButton;
    @FXML
    TextField idField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label signUpButton;

    private double x = 0;
    private double y = 0;
    private Stage stage;
    private StageUtil stageUtil = new StageUtil();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stageMove();
        enterEvent();
        loginButtonEvent();
        singUpEvent(primaryStage);
        stageUtil.importBasicsEvent(acp, stage, closeButton,true);
        stageUtil.hideButton(hideButton, acp);
    }

    private void stageMove() {//https://ohtanja.tistory.com/90 참고함
        acp.setOnMousePressed((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        acp.setOnMouseDragged((event) -> {
            stage = (Stage) acp.getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        acp.setOnMouseReleased((event) -> {
            stage = (Stage) acp.getScene().getWindow();
        });
    }


    private void enterEvent(){
        idField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode()== KeyCode.ENTER){
                passwordField.requestFocus();
            }
        });
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                if(!idField.getText().equals("")){
                  login();
                }else{
                    idField.requestFocus();
                }
            }
        });
    }

    private void loginButtonEvent(){
        loginButton.setOnMouseClicked(event -> {
            login();
        });
    }

    private void login(){
        System.out.println("로그인 요청 전송");
        try {
            SocketController.getInstance().login(idField.getText(),passwordField.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void singUpEvent(Stage ownerStage){
        signUpButton.setOnMouseClicked(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/signup.fxml"));
            Scene popupScene;
            Stage popup = new Stage();
            SignUpController.setStage(popup);
            try {
                popupScene = new Scene(fxmlLoader.load(), 400, 600);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setStageUtil(popup);
            popup.setTitle("L-Talk 회원가입");
            popup.initOwner(ownerStage); // 소유자 창 설정
            popup.setScene(popupScene);
            popup.initModality(javafx.stage.Modality.APPLICATION_MODAL); // 모달 창 설정
            popup.show();
        });
    }

}
