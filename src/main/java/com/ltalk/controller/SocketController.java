package com.ltalk.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.entity.Data;
import com.ltalk.entity.Friend;
import com.ltalk.enums.ProtocolType;
import com.ltalk.entity.ServerResponse;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import com.ltalk.util.LocalDateTimeAdapter;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.ltalk.Main.threadPool;

public class SocketController {

    static SocketController socketController;
    private static final Socket SOCKET = new Socket();
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private final String IP = "localhost";
    private final int PORT = 7623;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static SocketController getInstance() {
        if(socketController == null){
            socketController = new SocketController();
            try{
                socketController.connect();
                socketController.receive();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return socketController;
    }

    public void connect() throws IOException {
        SOCKET.connect(new InetSocketAddress(IP, PORT));
        outputStream=SOCKET.getOutputStream();
        inputStream=SOCKET.getInputStream();
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
        send(data);
    }

    public void login(String username, String password) throws IOException {
        Data data = new Data(ProtocolType.LOGIN, new LoginRequest(username, password));
        send(data);
    }

    public void chat(String receiver, String sender, String message) throws IOException {
        Data data = new Data(ProtocolType.CHAT, new ChatRequest(receiver, sender, message));
        send(data);
    }

    public void send(Data data) throws IOException {
        String dataString = gson.toJson(data);
        byte[] buffer = dataString.getBytes();
        outputStream.write(buffer);
        outputStream.flush();
    }

    public void receive() throws IOException {
        Runnable runnable = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] buffer = new byte[1024];
                    int bufferLength = inputStream.read(buffer);
                    if(bufferLength == -1) {
                        System.out.println("["+Thread.currentThread().getName()+"] <= data read Error");
                    }else{
                        String dataString = new String(buffer, 0, bufferLength, "UTF-8");
                        System.out.println("ServerResponse => " + dataString);
                        ServerResponse response = gson.fromJson(dataString, ServerResponse.class);
                        switch (response.getProtocolType()) {
                            case CHAT -> chatResponse(response);
                            case LOGIN -> loginResponse(response);
                            case SIGNUP -> signupResponse(response);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        threadPool.submit(runnable);
    }

    static public void disconnect() throws IOException {
        if(inputStream != null){
            inputStream.close();
        }
        if(outputStream != null){
            outputStream.close();
        }
        if(!SOCKET.isClosed()){
            SOCKET.close();
        }
        Platform.exit();
        threadPool.shutdown();
    }


}
