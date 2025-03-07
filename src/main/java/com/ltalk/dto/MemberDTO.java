package com.ltalk.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberDTO {

    private Long id;
    private String username;
    private String email;
    private List<FriendDTO> friends;
    private List<ChatRoomDTO> chatRoom;

}
