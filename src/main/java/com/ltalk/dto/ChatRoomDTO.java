package com.ltalk.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ChatRoomDTO {
    private Long id;
    private String name;
    private LocalDateTime lastChattedAt;
    private Integer participantCount = 0;
    @Setter
    private List<ChatRoomMemberDTO> members = Collections.synchronizedList(new ArrayList<>());
    private List<ChatDTO> chats = Collections.synchronizedList(new ArrayList<>());

}
