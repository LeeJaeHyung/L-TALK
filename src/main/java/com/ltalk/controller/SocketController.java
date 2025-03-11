package com.ltalk.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltalk.entity.Data;
import com.ltalk.entity.ServerResponse;
import com.ltalk.enums.ProtocolType;
import com.ltalk.handler.WriteHandler;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.DisconnectRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import com.ltalk.service.DataService;
import com.ltalk.service.SocketService;
import com.ltalk.util.LocalDateTimeAdapter;
import javafx.application.Platform;
import lombok.Getter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class SocketController {
    private static AsynchronousChannelGroup group;
    public static boolean isConnected = false;
    public SocketService socketService = new SocketService();
    public DataService dataService = new DataService();

    static {
        try {
            group = AsynchronousChannelGroup.withFixedThreadPool(4, Executors.defaultThreadFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    private static AsynchronousSocketChannel channel;

    static {
        try {
            channel = AsynchronousSocketChannel.open(group);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 7623;
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

    private void connect() throws IOException, ExecutionException, InterruptedException {
        socketService.connect(channel);
    }

    private void signupResponse(ServerResponse response) {
        System.out.println(response.getStatus()+"<<<");
        if (response.getStatus()) {
            Platform.runLater(() -> {
                SignUpController.singUpStage.close();
            });
        }else{
            System.out.println(response.getSignupResponse().getMsg());
            //회원가입 실패 사유 알려주기
        }
    }

    public void interpret(ServerResponse response) throws NoSuchAlgorithmException, IOException {
        dataService.interpret(response);
    }

    private void chatResponse(ServerResponse response) {
        if(response.getStatus()){
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

    public void chat(Long chatRoomId, Long senderId, String message) throws IOException {
        Data data = new Data(ProtocolType.CHAT, new ChatRequest(chatRoomId, senderId, message));
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
                isConnected = false;
                if(MainController.member==null){
                    sendData(new Data(ProtocolType.DISCONNECT, new DisconnectRequest()));
                }else{
                    sendData(new Data(ProtocolType.DISCONNECT, new DisconnectRequest(MainController.getMember().getUsername())));
                }
                channel.close();
                group.shutdownNow();
                System.out.println("서버와 연결이 종료되었습니다.");

            } catch (IOException e) {
                System.err.println("연결 종료 중 오류 발생: " + e.getMessage());
            }
        }
    }


}
