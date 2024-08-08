package com.ltalk.response;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String msg;
    public LoginResponse(String msg) {
        this.msg = msg;
    }
}
