package com.back.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userUniqueIdentifier;
    private String password;
}
