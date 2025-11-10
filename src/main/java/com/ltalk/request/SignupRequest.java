package com.ltalk.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupRequest {
    String username;
    String password;
    String email;


}
