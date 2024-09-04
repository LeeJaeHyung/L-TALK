package com.ltalk.entity;

import com.ltalk.enums.FriendStatus;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "friend_name", nullable = false)
    private String friend_name;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendStatus status = FriendStatus.PENDING;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Friend(Member member, String friend_name, FriendStatus status){
        this.username = member.getUsername();
        this.friend_name = friend_name;
        this.status = status;
    }

    public Friend() {
    }
}
