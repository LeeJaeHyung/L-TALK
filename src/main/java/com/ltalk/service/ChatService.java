package com.ltalk.service;

import com.ltalk.controller.ChatController;
import com.ltalk.controller.SocketController;
import com.ltalk.entity.Data;
import com.ltalk.enums.ProtocolType;
import com.mysql.cj.admin.ServerController;

import javax.sql.RowSet;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

import static com.ltalk.controller.MainController.member;
import static com.ltalk.controller.SocketController.getInstance;
import static com.ltalk.controller.SocketController.socketController;

public class ChatService {
    SocketController channel = getInstance();

    public ChatService() throws IOException {
    }

    public void getChatList() throws IOException {
        socketController.sendData(new Data(ProtocolType.CHAT_LIST, member.getUsername()));
    }
}
