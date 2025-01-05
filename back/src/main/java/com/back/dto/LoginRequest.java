package com.back.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userUniqueIdentifier;
    private String password;
}
