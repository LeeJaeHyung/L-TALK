package com.ltalk.entity;

import com.ltalk.enums.ProtocolType;
import com.ltalk.request.ChatRequest;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import lombok.Getter;

@Getter
public class Data {
    ProtocolType protocolType;
    ChatRequest chatRequest;
    LoginRequest loginRequest;
    SignupRequest signupRequest;

    public Data(ProtocolType protocolType, ChatRequest chatRequest) {
        this.protocolType = protocolType;
        this.chatRequest = chatRequest;
    }

    public Data(ProtocolType protocolType, LoginRequest loginRequest) {
        this.protocolType = protocolType;
        this.loginRequest = loginRequest;
    }

    public Data(ProtocolType protocolType, SignupRequest signupRequest) {
        this.protocolType = protocolType;
        this.signupRequest = signupRequest;
    }
}
