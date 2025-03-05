package com.ltalk.dto;

import com.ltalk.enums.FriendStatus;
import lombok.Getter;

@Getter
public class FriendDTO {
    private Long friendId;
    private String friendName;
    private FriendStatus status;

}
