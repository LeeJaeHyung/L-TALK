package com.ltalk.service;

import com.ltalk.controller.ChatController;
import com.ltalk.controller.SocketController;
import com.ltalk.entity.Data;
import com.ltalk.enums.ProtocolType;
import com.mysql.cj.admin.ServerController;

import javax.sql.RowSet;

import java.io.IOException;

import static com.ltalk.controller.MainController.member;
import static com.ltalk.controller.SocketController.getInstance;

public class ChatService {
    SocketController socketController = getInstance();

    public void getChatList() throws IOException {
        socketController.send(new Data(ProtocolType.CHAT_LIST, member.getUsername()));
    }
}
