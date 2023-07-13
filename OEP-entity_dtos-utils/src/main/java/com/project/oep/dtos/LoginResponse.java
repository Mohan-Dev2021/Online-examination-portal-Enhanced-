package com.project.oep.dtos;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String userName;
    private String emailId;
    private String role;


    public LoginResponse() {
        super();
    }
}