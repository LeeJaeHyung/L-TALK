package com.ltalk.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.entity.Data;
import com.ltalk.entity.Friend;
import com.ltalk.entity.ServerResponse;
import com.ltalk.enums.ProtocolType;
import com.ltalk.handler.ConnectionHandler;
import com.ltalk.handler.WriteHandler;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import com.ltalk.util.LocalDateTimeAdapter;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class SocketController {
    private static AsynchronousChannelGroup group;
    public static boolean isConnected = false;

    static {
        try {
            group = AsynchronousChannelGroup.withFixedThreadPool(4, Executors.defaultThreadFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static AsynchronousSocketChannel channel;

    static {
        try {
            channel = AsynchronousSocketChannel.open(group);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 7623;
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    public static SocketController socketController;
    public SocketController() throws IOException {

    }

    public static SocketController getInstance() throws IOException {
        if(socketController == null){
            socketController = new SocketController();
            try{
                socketController.connect();
            }catch (IOException | ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }
        return socketController;
    }

    private static void connect() throws IOException, ExecutionException, InterruptedException {
        channel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), channel, new ConnectionHandler(channel));
    }

    private void signupResponse(ServerResponse response) {
        System.out.println(response.getSuccess()+"<<<");
        if (response.getSuccess()) {
            Platform.runLater(() -> {
                SignUpController.singUpStage.close();
            });
        }else{
            System.out.println(response.getSignupResponse().getMsg());
            //회원가입 실패 사유 알려주기
        }
    }

    private void loginResponse(ServerResponse response) {
        System.out.println(response.getLoginResponse().getMsg());
        if(response.getSuccess()){
            //메인 화면 보여주기
            Set<Friend> freindList =response.getLoginResponse().getMember().getFriends();
            for (Friend friend : freindList) {
                System.out.println(friend.getFriend().getUsername());
            }
            Platform.runLater(() ->{
                LTalkController.getPrimaryStage().close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
                Scene scene = null;
                MainController.setMember(response.getLoginResponse().getMember());
                MainController.setFriendList(response.getLoginResponse().getMember().getFriends());
                Stage stage = new Stage();
                MainController.setStage(stage);
                try {
                    scene = new Scene(fxmlLoader.load(), 360, 590);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("L-Talk");
                stage.setScene(scene);
                StageUtil.setStageUtil(stage);
                stage.show();
            });
            System.out.println("로그인 성공");
        }else{
            //실패 사유 보여주기
            System.out.println("로그인 실패");
        }
    }

    private void chatResponse(ServerResponse response) {
        if(response.getSuccess()){
            //받아온 채팅 추가하는 로직 불러오기
            System.out.println("수신성공");
            System.out.println(response.toString());
        }else{
            //채팅 전송 실패 사유
        }
    }

    public void signup(String username, String password, String email) throws IOException {
        Data data = new Data(ProtocolType.SIGNUP, new SignupRequest(username, password, email));
        sendData(data);
    }

    public void login(String username, String password) throws IOException {
        Data data = new Data(ProtocolType.LOGIN, new LoginRequest(username, password));
        sendData(data);
    }

    public void chat(String receiver, String sender, String message) throws IOException {
        Data data = new Data(ProtocolType.CHAT, new ChatRequest(receiver, sender, message));
        sendData(data);
    }


    public static void sendData(Data data) {
        String jsonData = gson.toJson(data);
        ByteBuffer writeBuffer = ByteBuffer.wrap(jsonData.getBytes());
        channel.write(writeBuffer, writeBuffer, new WriteHandler(channel,writeBuffer));
    }



    public static void disconnect() {
        if (isConnected) {
            try {
                group.shutdownNow();
                channel.close();
                System.out.println("서버와 연결이 종료되었습니다.");
            } catch (IOException e) {
                System.err.println("연결 종료 중 오류 발생: " + e.getMessage());
            }
        }
    }


}
