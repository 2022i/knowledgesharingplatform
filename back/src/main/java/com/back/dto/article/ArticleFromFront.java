package com.back.dto.article;

import lombok.Data;

@Data
public class ArticleFromFront {
    private int authorId;
    private int themeId;
    private String title;
    private String content;
    private String summary;
    private String relatedKnowledge;
}
