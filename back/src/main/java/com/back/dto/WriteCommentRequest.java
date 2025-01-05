package com.back.dto;

import lombok.Data;

@Data
public class WriteCommentRequest {
    private String content;
    private int userId;
    private int articleId;
    private int fatherId;
}
