package com.back.dto.article;

import com.back.dto.ArticleAuthor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RenderedArticle {
    private int id;
    private String title;
    private ArticleAuthor Author;
    private String theme;
    private String content;
    private String summary;
    private List<String> relatedKnowledge=new ArrayList<>();
    private LocalDateTime createTime;
    private int viewUserCount;
    private int supportUserCount;
    private int opposeUserCount;
    private int commentCount;
    private int collectionUserCount ;
    private int shareUserCount;
}
