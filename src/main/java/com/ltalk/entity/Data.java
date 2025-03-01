package com.ltalk.entity;

import com.ltalk.enums.ProtocolType;
import com.ltalk.request.*;
import lombok.Getter;

@Getter
public class Data {
    ProtocolType protocolType;
    ChatRequest chatRequest;
    LoginRequest loginRequest;
    SignupRequest signupRequest;
    DisconnectRequest disconnectRequest;
    FriendRequest friendRequest;
    String username;

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

    public Data(ProtocolType protocolType, String username){
        this.protocolType = protocolType;
        this.username = username;
    }

    public Data(ProtocolType protocolType, DisconnectRequest disconnectRequest) {
        this.protocolType = protocolType;
        this.disconnectRequest = disconnectRequest;
    }

    public Data(ProtocolType protocolType, FriendRequest friendRequest) {
        this.protocolType = protocolType;
        this.friendRequest = friendRequest;
    }
}
