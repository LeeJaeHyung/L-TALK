package com.ltalk.service;

import com.ltalk.controller.MainController;
import com.ltalk.controller.SocketController;
import com.ltalk.entity.Data;
import com.ltalk.enums.ChatRoomType;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.ChatRoomCreatRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ltalk.controller.SocketController.getInstance;
import static com.ltalk.controller.SocketController.sendData;

public class ChatService {
    SocketController channel = getInstance();

    public ChatService() throws IOException {
    }

//    public void getChatList() throws IOException {
//        SocketController.sendData(new Data(ProtocolType.CHAT_LIST, member.getUsername()));
//    }

    public void creatRoom() {
        ChatRoomType roomType = ChatRoomType.PRIVATE;
        String chatName = "테스트용";
        List<String> chatRoomMembers = new ArrayList<>();
        chatRoomMembers.add("asd");
        chatRoomMembers.add(MainController.getMember().getUsername());
        ChatRoomCreatRequest chatRoomCreatRequest = new ChatRoomCreatRequest(chatName, roomType ,chatRoomMembers);
        sendData(new Data(ProtocolType.CREATE_CHATROOM, chatRoomCreatRequest));
    }
}
