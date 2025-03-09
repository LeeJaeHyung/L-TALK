package com.ltalk.dto;

import com.ltalk.controller.MainController;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ChatDTO {
    private long chatRoomId;
    private long chatId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    public ChatDTO(String message) {
        this.sender = MainController.getMember().getUsername();
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

}
