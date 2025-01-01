package com.ltalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Set<ChatRoomMember> members;
}