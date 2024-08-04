package com.ltalk;

import com.ltalk.controller.LTalkController;
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
    public static ExecutorService threadPool;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 590);
        stage.setTitle("L-Talk");
        stage.setScene(scene);
        setStageUtil(stage);
        stage.show();
        LTalkController.setPrimaryStage(stage);
        threadPool = Executors.newFixedThreadPool(10);
    }
}
