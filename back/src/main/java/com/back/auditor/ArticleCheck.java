package com.back.auditor;

import com.back.dto.Response;
import com.back.dto.Article.UnCheckArticle;
import com.back.get.UnCheckArticlePreparation;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ArticleCheck {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private UnCheckArticlePreparation unCheckArticlePreparation;
    public List<UnCheckArticle> getUnCheckArticles() {
        List<Article> uncheckArticles = articleRepository.findArticlesByCheck(false);
        uncheckArticles.sort(Comparator.comparing(Article::getCreateTime));
        return unCheckArticlePreparation.getUnCheckArticles(uncheckArticles);
    }
    public Response approveArticle(int articleId) {
        Article article = articleRepository.findArticleById(articleId);
        article.setCheck(true);
        articleRepository.save(article);
        return Response.successApproveArticle();
    }
    public Response rejectArticle(int articleId) {
        articleRepository.deleteArticleById(articleId);
        return Response.successRejectArticle();
    }
}
