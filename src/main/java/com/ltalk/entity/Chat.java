package com.ltalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Chat {

    private Long chatId;

    private ChatRoom chatRoom;

    private Member sender; // 메시지 보낸 사람 추가

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
