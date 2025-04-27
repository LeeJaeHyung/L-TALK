package com.ltalk.response;

import com.ltalk.dto.ChatRoomDTO;
import lombok.Getter;

@Getter
public class CreateChatRoomResponse {
    ChatRoomDTO chatRoomDTO;
    public CreateChatRoomResponse(ChatRoomDTO chatRoomDTO) {
        this.chatRoomDTO = chatRoomDTO;
    }
}
