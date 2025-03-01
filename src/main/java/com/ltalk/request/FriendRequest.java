package com.ltalk.request;

import java.time.LocalDateTime;

public class FriendRequest {
    String member;
    String friend;
    LocalDateTime RequestTime;

    public FriendRequest(String member, String friend){
        this.member = member;
        this.friend = friend;
        RequestTime = LocalDateTime.now();
    }
}
