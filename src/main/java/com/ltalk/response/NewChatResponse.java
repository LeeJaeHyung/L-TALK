package com.ltalk.response;

import com.ltalk.dto.ChatDTO;
import lombok.Getter;

@Getter
public class NewChatResponse {
    ChatDTO dto;
    public NewChatResponse(ChatDTO dto) {
        this.dto = dto;
    }
}
