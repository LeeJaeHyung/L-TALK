package com.ltalk.entity;

import com.ltalk.enums.FriendStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class Friend {

    Long id;
    private Member member;
    private Member friend;
    private FriendStatus status = FriendStatus.PENDING;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Friend(Member member, Member friend, FriendStatus status) {
        this.member = member;
        this.friend = friend;
        this.status = status;
    }

    public Friend() {
    }
}
