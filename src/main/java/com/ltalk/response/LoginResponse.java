package com.ltalk.response;

import com.ltalk.entity.Friend;
import com.ltalk.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String msg;
    private Member member;
    private List<String> friends;

    public LoginResponse(String msg){
        this.msg = msg;
    }
    public LoginResponse(Member member, String msg) {
        this.msg = msg;
        this.member = member;
    }
}
