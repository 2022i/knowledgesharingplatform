package com.back.dto.Article;

import lombok.Data;

@Data
public class ArticleFromFront {
    private int authorId;
    private int themeId;
    private String title;
    private String content;
}
