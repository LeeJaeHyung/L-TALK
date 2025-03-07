package com.ltalk.request;

import java.time.LocalDateTime;

public class ChatRequest {
    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime sendDate;

    public ChatRequest(Long chatRoomId, Long senderId, String message) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }
}
