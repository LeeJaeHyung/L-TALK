package com.ltalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class Member {

    private Long id;

    private String username;

    private String password;

    private String email;

    private Set<ChatRoomMember> chatRooms = new HashSet<>();

    private Set<Friend> friends = new HashSet<>();

}
