package com.ltalk.dto;

import lombok.Getter;

@Getter
public class ChatRoomMemberDTO {
    private Long id;
    private Long memberId;
    private Long chatRoomMemberId;
    private String memberName;
    private Long readChatId;
}
