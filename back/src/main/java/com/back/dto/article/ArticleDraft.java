package com.back.dto.article;

import lombok.Data;

@Data
public class ArticleDraft {
    private String title;
    private String content;
    private int themeId;
}
