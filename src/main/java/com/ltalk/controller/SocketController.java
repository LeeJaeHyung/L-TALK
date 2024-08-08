package com.ltalk.controller;

import com.google.gson.Gson;
import com.ltalk.entity.Data;
import com.ltalk.entity.ProtocolType;
import com.ltalk.entity.ServerResponse;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.ltalk.Main.threadPool;

public class SocketController {

    static SocketController socketController;
    private static final Socket SOCKET = new Socket();
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private final String IP = "localhost";
    private final int PORT = 7623;
    private Gson gson = new Gson();
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
        if(response.getSuccess()){
            SignUpController.singUpStage.close();
        }else{
            System.out.println(response.getSignupResponse().getMsg());
            //회원가입 실패 사유 알려주기
        }
    }

    private void loginResponse(ServerResponse response) {
        System.out.println(response.getLoginResponse().getMsg());
        if(response.getSuccess()){
            //메인 화면 보여주기
            System.out.println("로그인 성공");
        }else{
            //실패 사유 보여주기
            System.out.println("로그인 실패");
        }
    }

    private void chatResponse(ServerResponse response) {
        if(response.getSuccess()){
            //받아온 채팅 추가하는 로직 불러오기
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
                }
            }
        };
        threadPool.submit(runnable);
    }




}
