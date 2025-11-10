package com.ltalk.response;


import java.util.List;
import com.ltalk.dto.FriendDTO;
import lombok.Getter;

@Getter
public class ChatRoomCreationCheckResponse {
    List<FriendDTO> friendDTOList;

    public ChatRoomCreationCheckResponse(List<FriendDTO> friendDTOList) {
        this.friendDTOList = friendDTOList;
    }

}
