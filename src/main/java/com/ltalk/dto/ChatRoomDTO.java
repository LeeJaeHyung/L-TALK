package com.ltalk.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatRoomDTO {
    private Long id;
    private String name;
    private LocalDateTime lastChattedAt;
    private Integer participantCount = 0;
    private List<ChatRoomMemberDTO> members = new ArrayList<>();
    private List<ChatDTO> chats = new ArrayList<>();

}
