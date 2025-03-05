package com.ltalk.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ChatDTO {
    private long chatId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

}
