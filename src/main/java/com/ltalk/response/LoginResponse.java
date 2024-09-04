package com.ltalk.response;

import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String msg;
    private List<Friend> friendList;
    private Member member;

    public LoginResponse(String msg){
        this.msg = msg;
    }
    public LoginResponse(Member member, List<Friend> friendList, String msg) {
        this.friendList = friendList;
        this.msg = msg;
        this.member = member;
    }
}
