package com.ltalk.entity;

import com.ltalk.enums.ProtocolType;
import com.ltalk.response.*;
import lombok.Getter;

@Getter
public class ServerResponse {
    private ProtocolType protocolType;
    private boolean status;
    private ChatResponse chatResponse;
    private LoginResponse loginResponse;
    private SignupResponse signupResponse;
    private NewChatResponse newChatResponse;
    private ReadChatResponse readChatResponse;
    private VoiceServerIPResponse voiceServerIPResponse;

    public ServerResponse(ProtocolType protocolType, boolean status, ChatResponse chatResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.chatResponse = chatResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean status, LoginResponse loginResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.loginResponse = loginResponse;
    }
    public ServerResponse(ProtocolType protocolType, boolean status, SignupResponse signupResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.signupResponse = signupResponse;
    }

    public boolean getStatus() {
        return status;
    }
    public ServerResponse(ProtocolType protocolType, boolean status, NewChatResponse newChatResponse) {
        this.protocolType = protocolType;
        this.status = status;
        this.newChatResponse = newChatResponse;
    }
}
