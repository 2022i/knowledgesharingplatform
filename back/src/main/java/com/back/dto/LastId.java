package com.back.dto;

import lombok.Data;

@Data
public class LastId {
    private static int userId =0;
    private static int commentId =0;
    public static int getUserId() {
        userId++;
        return userId;
    }
    public static int getCommentId() {
        commentId++;
        return commentId;
    }
}
