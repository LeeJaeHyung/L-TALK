package com.ltalk;

import com.ltalk.controller.LTalkController;
import com.ltalk.controller.SocketController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ltalk.util.StageUtil.setStageUtil;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LTalkController.setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 590);
        stage.setTitle("L-Talk");
        stage.setScene(scene);
        setStageUtil(stage);

        // LTalkController에 stage 설정
        LTalkController lTalkController = fxmlLoader.getController();
        lTalkController.setStage(stage);
        stage.show();
        SocketController.getInstance();
    }
}
