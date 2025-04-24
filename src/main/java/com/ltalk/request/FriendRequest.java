package com.ltalk.request;

import com.ltalk.controller.MainController;
import com.ltalk.dto.FriendDTO;

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

    public FriendRequest(FriendDTO dto) {
        this.member = MainController.getMember().getUsername();
        this.friend = dto.getFriendName();
        RequestTime = LocalDateTime.now();
    }
}
