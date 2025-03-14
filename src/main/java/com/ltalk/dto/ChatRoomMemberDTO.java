package com.ltalk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatRoomMemberDTO {
    private Long id;
    private Long memberId;
    private Long chatRoomId;
    private String memberName;
    @Setter
    private Long readChatId;
}
