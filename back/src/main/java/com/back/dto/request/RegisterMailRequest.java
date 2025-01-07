package com.back.dto.request;

import lombok.Data;

@Data
public class RegisterMailRequest {
    private String username;
    private String email;
}
