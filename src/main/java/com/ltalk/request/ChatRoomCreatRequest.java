package com.ltalk.request;

import com.ltalk.enums.ChatRoomType;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomCreatRequest {
    private String roomName;
    private ChatRoomType roomType;
    private List<String> chatRoomMembers;

    public ChatRoomCreatRequest(String roomName, ChatRoomType roomType, List<String> chatRoomMembers) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.chatRoomMembers = chatRoomMembers;
    }
}
