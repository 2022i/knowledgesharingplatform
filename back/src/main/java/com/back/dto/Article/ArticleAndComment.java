package com.back.dto.Article;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleAndComment {
    private RenderedArticle article=new RenderedArticle();
    private List<String> comments=new ArrayList<>();
}
