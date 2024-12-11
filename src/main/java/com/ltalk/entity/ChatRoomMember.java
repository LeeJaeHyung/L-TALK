package com.ltalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomMember {

    private Long id;

    private ChatRoom chatRoom;

    private Member member;

    private LocalDateTime joinedAt;
}

