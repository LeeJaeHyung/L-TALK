package com.ltalk.controller;

import com.google.gson.Gson;
import com.ltalk.entity.Data;
import com.ltalk.entity.ProtocolType;
import com.ltalk.request.SignupRequest;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketController {

    static SocketController socketController;
    private static final Socket SOCKET = new Socket();
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private final String IP = "localhost";
    private final int PORT = 7623;
    private Gson gson = new Gson();

    public SocketController() {
        socketController = this;
        try {
            connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() throws IOException {
        SOCKET.connect(new InetSocketAddress(IP, PORT));
        outputStream=SOCKET.getOutputStream();
        inputStream=SOCKET.getInputStream();
    }

    public void signup() throws IOException {
        Data data = new Data();
        data.setSignupRequest(new SignupRequest("아몰랑", "비밀번호","이메일"));
        data.setProtocolType(ProtocolType.SIGNUP);
        String dataString = gson.toJson(data);
        byte[] buffer = dataString.getBytes();
        outputStream.write(buffer);
        outputStream.flush();
    }




}
