package com.back.dto.Article;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UnCheckArticle {
    private int id;
    private int authorId;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime createTime;
}
