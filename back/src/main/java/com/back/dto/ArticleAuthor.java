package com.back.dto;

import lombok.Data;

@Data
public class ArticleAuthor {
    private int id;
    private String username;
    private boolean isFollowed;
}
