package com.back.auditor;

import com.back.dto.Article.AuditorArticle;
import com.back.get.AuditorArticlePreparation;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class UnCheckArticleGet {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private AuditorArticlePreparation auditorArticlePreparation;
    public List<AuditorArticle> getUnCheckArticles() {
        List<Article> uncheckArticles = articleRepository.findArticlesByCheck(false);
        uncheckArticles.sort(Comparator.comparing(Article::getCreateTime));
        return auditorArticlePreparation.getUnCheckArticles(uncheckArticles);
    }
    public AuditorArticle getUncheckArticle(int articleId) {
        return auditorArticlePreparation.getUnCheckArticle(articleRepository.findArticleById(articleId));
    }
}
