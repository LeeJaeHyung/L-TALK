package com.ltalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false , unique = true )
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true )
    private String email;

    private Set<ChatRoomMember> chatRooms = new HashSet<>();
    private Set<Friend> friends = new HashSet<>();

}
