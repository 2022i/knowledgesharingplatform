package com.back.get.ArtilcesList;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class ArticlesList {
    @Resource
    protected ArticleRepository articlerepository;
    protected List<Article> getArticlesList(List<Integer> articleIds) {
        List<Article> articles = new ArrayList<>();
        for(Integer id : articleIds){
            articles.add(articlerepository.findArticleById(id));
        }
        return articles;
    }
    abstract List<Article> getArticlesList(int userId);
}
