package com.ltalk.dto;

import lombok.Getter;

@Getter
public class ChatRoomMemberDTO {
    private Long id;
    private Long memberId;
    private Long chatRoomId;
    private String memberName;
    private Long readChatId;
}
