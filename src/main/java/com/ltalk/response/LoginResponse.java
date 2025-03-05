package com.ltalk.response;

import com.ltalk.dto.ChatRoomDTO;
import com.ltalk.dto.FriendDTO;
import com.ltalk.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String msg;
    private Member member;
    private List<FriendDTO> friends;
    private List<ChatRoomDTO> chatRoom;

}
