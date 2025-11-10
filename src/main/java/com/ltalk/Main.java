package com.ltalk;

import com.ltalk.controller.LTalkController;
import com.ltalk.controller.SocketController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.ltalk.controller.MainController.voiceRecieveThread;
import static com.ltalk.controller.MainController.voiceSendThread;
import static com.ltalk.service.VoiceService.receiveSocket;
import static com.ltalk.service.VoiceService.sendSocket;
import static com.ltalk.util.StageUtil.setStageUtil;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
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

    @Override
    public void stop() throws Exception {

        SocketController socketController = SocketController.getInstance();
        if (socketController != null) {
            SocketController.disconnect();  // ← 수정됨!
        }

        // 송수신 쓰레드 안전하게 중단
        if (voiceRecieveThread != null && voiceRecieveThread.isAlive()) {
            voiceRecieveThread.interrupt();
        }

        if (voiceSendThread != null && voiceSendThread.isAlive()) {
            voiceSendThread.interrupt();
        }

        // UDP 소켓 닫기
        if (receiveSocket != null && !receiveSocket.isClosed()) {
            receiveSocket.close();
        }
        if(sendSocket != null && !sendSocket.isClosed()){
            sendSocket.close();
        }

        super.stop();
    }
}
