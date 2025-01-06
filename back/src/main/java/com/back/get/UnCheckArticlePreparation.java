package com.back.get;

import com.back.dto.UnCheckArticle;
import com.back.index.Article;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnCheckArticlePreparation {
    public List<UnCheckArticle> getUnCheckArticles(List<Article> articles){
        List<UnCheckArticle> unCheckArticles = new ArrayList<>();
        for(Article article : articles){
            unCheckArticles.add(getUnCheckArticle(article));
        }
        return unCheckArticles;
    }
    private UnCheckArticle getUnCheckArticle(Article article){
        UnCheckArticle unCheckArticle = new UnCheckArticle();
        unCheckArticle.setId(article.getId());
        unCheckArticle.setAuthorId(article.getAuthorId());
        unCheckArticle.setTitle(article.getTitle());
        unCheckArticle.setContent(article.getContent());
        unCheckArticle.setSummary(article.getSummary());
        unCheckArticle.setCreateTime(article.getCreateTime());
        return unCheckArticle;
    }
}
