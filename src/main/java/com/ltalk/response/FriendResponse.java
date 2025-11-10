package com.ltalk.response;

import com.ltalk.entity.Friend;

import java.util.List;

public class FriendResponse {

    private List<Friend> friendList;

    public FriendResponse(List<Friend> friendList) {
        this.friendList = friendList;
    }
}
