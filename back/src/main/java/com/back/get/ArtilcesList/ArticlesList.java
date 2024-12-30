package com.back.get.ArtilcesList;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;

import java.util.List;

public abstract class ArticlesList {
    @Resource
    protected ArticleRepository articlerepository;

    abstract List<Article> getArticlesList(int userId);
}
