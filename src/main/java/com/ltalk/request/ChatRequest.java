package com.ltalk.request;

import java.time.LocalDateTime;

public class ChatRequest {
    private String receiver;
    private String sender;
    private String message;
    private LocalDateTime sendDate;

    public ChatRequest(String receiver, String sender, String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }
}
