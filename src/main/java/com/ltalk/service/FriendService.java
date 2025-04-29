package com.ltalk.service;

import com.ltalk.controller.MainController;
import com.ltalk.dto.FriendDTO;
import com.ltalk.entity.Data;
import com.ltalk.enums.ProtocolType;
import com.ltalk.request.FriendRequest;
import com.ltalk.request.FriendSearchRequest;

import static com.ltalk.controller.SocketController.sendData;

public class FriendService {

    public void searchFriend(String userName){
        sendData(new Data(ProtocolType.FRIEND_SEARCH, new FriendSearchRequest(userName)));
    }


    public void requestFriend(FriendDTO dto) {
        if (!MainController.member.getUsername().equals(dto.getFriendName())){
            sendData(new Data(ProtocolType.REQUEST_FRIEND, new FriendRequest(dto)));
        }
    }
}
