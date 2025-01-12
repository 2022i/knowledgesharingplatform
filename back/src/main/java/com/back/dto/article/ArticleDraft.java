package com.back.dto.article;

import com.back.index.Theme;
import lombok.Data;

@Data
public class ArticleDraft {
    private int id;
    private String title;
    private String content;
    private Theme theme;
}
