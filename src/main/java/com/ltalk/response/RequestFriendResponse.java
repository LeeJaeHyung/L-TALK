package com.ltalk.response;

import com.ltalk.dto.FriendDTO;
import lombok.Getter;

@Getter
public class RequestFriendResponse {
    FriendDTO friendDTO;
    public RequestFriendResponse(FriendDTO friendDTO) {
        this.friendDTO = friendDTO;
    }
}
