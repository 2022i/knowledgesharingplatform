package com.back.dto.article;

import lombok.Data;

import java.util.List;

@Data
public class ArticleFromFront {
    private int authorId;
    private int themeId;
    private String title;
    private String content;
    private String summary;
    private List<String> relatedKnowledge;
}
