package com.ltalk.request;

public class DisconnectRequest {

    String key;

    public DisconnectRequest(){
        key = null;
    }

    public DisconnectRequest(String key) {
        this.key = key;
    }
}
