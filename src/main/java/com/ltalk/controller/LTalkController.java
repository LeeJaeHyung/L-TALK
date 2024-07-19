package com.ltalk.controller;

import com.ltalk.entity.Member;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stageMove();
        hideButton();
        enterEvent();
        singUpEvent(primaryStage);
        closeButton.setOnAction(event -> Platform.exit());
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

    private void hideButton() {
        hideButton.setOnAction(event -> {stage = (Stage) acp.getScene().getWindow(); stage.setIconified(true);});
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
                    testConnection();
                }else{
                    idField.requestFocus();
                }
            }
        });
    }

    private void login(){

    }

    private void testConnection(){
        System.out.println("테스트 실행");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ltalk");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("lese");
            member.setPassword("1234sd56");
            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
        System.out.println("처리완료");

    }

    private void singUpEvent(Stage ownerStage){
        signUpButton.setOnMouseClicked(event -> {
            Label message = new Label("This is a popup window!");
            Button closeButton = new Button("Close");
            VBox popupLayout = new VBox(10, message, closeButton);
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
            closeButton.setOnAction(e -> popup.close());
            popup.show();
        });
    }

}
