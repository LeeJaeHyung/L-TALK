package com.ltalk.response;

import com.ltalk.dto.ChatRoomMemberDTO;
import lombok.Getter;

@Getter
public class ReadChatResponse {

    ChatRoomMemberDTO roomMember;

    public ReadChatResponse(ChatRoomMemberDTO chatRoomMemberDTO) {
        this.roomMember = chatRoomMemberDTO;
    }
}
