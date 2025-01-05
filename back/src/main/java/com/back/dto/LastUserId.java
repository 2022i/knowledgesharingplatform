package com.back.dto;

import lombok.Data;

@Data
public class LastUserId {
    private static int userId =0;
    public static int getId() {
        userId++;
        return userId;
    }
}
