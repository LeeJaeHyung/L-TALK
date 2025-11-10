package com.ltalk.response;

import com.ltalk.dto.FriendDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class FriendSearchResponse {
    List<FriendDTO> friendDTOList;
}
