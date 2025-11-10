package com.ltalk.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class ChatRoom {

    private Long chatRoomId;

    private String name;

    private Integer participantCount = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ChatRoomMember> members;

    private List<Chat> chats;

}
