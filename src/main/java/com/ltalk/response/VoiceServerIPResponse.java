package com.ltalk.response;

import lombok.Getter;

@Getter
public class VoiceServerIPResponse {
    String ip;
    int port;
    Long chatRoomId;
    public VoiceServerIPResponse(String ip, Long chatRoomId) {
        this.ip = ip;
        this.chatRoomId = chatRoomId;
    }
    public boolean isNull(){
        return ip==null;
    }
}
