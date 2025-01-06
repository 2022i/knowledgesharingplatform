package com.back.dto;

import lombok.Data;

@Data
public class FrontArticle {
    private int authorId;
    private int themeId;
    private String title;
    private String content;
}
