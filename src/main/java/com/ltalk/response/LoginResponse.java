package com.ltalk.response;

import com.ltalk.dto.MemberDTO;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String msg;
    private MemberDTO member;
}
