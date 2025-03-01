package com.ltalk.service;

import com.ltalk.controller.LTalkController;
import com.ltalk.controller.MainController;
import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import com.ltalk.entity.ServerResponse;
import com.ltalk.handler.ConnectionHandler;
import com.ltalk.util.StageUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.ltalk.controller.SocketController.SERVER_ADDRESS;
import static com.ltalk.controller.SocketController.SERVER_PORT;

@NoArgsConstructor
public class SocketService {

    public void connect(AsynchronousSocketChannel channel) throws IOException, ExecutionException, InterruptedException {
        channel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), channel, new ConnectionHandler(channel));
    }



}
