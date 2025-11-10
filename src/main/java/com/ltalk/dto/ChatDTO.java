package com.ltalk.dto;

import com.ltalk.controller.MainController;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ChatDTO {
    private Long chatRoomId;
    private Long chatId;
    private Long senderId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;
    @Setter
    private int unreadCount;

    public ChatDTO(String message) {
        this.sender = MainController.getMember().getUsername();
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

}
