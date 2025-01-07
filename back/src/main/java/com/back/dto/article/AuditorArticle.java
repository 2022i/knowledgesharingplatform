package com.back.dto.article;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditorArticle {
    private int id;
    private int authorId;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime createTime;
}
