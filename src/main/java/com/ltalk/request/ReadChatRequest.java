package com.ltalk.request;

public class ReadChatRequest {
    Long chatRoomId;
    Long chatId;
    Long memberId;
    String userName;

    public ReadChatRequest(Long chatRoomId, Long chatId, Long memberId, String userName) {
        this.chatRoomId = chatRoomId;
        this.chatId = chatId;
        this.memberId = memberId;
        this.userName = userName;
    }
}
