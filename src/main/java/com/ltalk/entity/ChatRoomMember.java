package com.ltalk.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ChatRoomMember {

    private Long chatRoomMemberId;

    private ChatRoom chatRoom;

    private Long memberId;

    private Long readChatId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
