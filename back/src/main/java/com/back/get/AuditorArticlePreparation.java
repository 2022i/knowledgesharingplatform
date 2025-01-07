package com.back.get;

import com.back.dto.article.AuditorArticle;
import com.back.index.Article;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditorArticlePreparation {
    public List<AuditorArticle>  getUnCheckArticles(List<Article> articles){
        List<AuditorArticle> auditorArticles = new ArrayList<>();
        for(Article article : articles){
            auditorArticles.add(getUnCheckArticle(article));
        }
        return auditorArticles;
    }
    public AuditorArticle getUnCheckArticle(Article article){
        AuditorArticle auditorArticle = new AuditorArticle();
        auditorArticle.setId(article.getId());
        auditorArticle.setAuthorId(article.getAuthorId());
        auditorArticle.setTitle(article.getTitle());
        auditorArticle.setContent(article.getContent());
        auditorArticle.setSummary(article.getSummary());
        auditorArticle.setCreateTime(article.getCreateTime());
        return auditorArticle;
    }
}
