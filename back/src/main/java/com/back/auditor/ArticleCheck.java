package com.back.auditor;

import com.back.dto.Response;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArticleCheck {
    @Resource
    private ArticleRepository articleRepository;
    public Response approveArticle(int articleId) {
        Article article = articleRepository.findArticleById(articleId);
        article.setCheck(true);
        articleRepository.save(article);
        return Response.successApproveArticle();
    }
    public Response rejectArticle(int articleId) {
        Article article = articleRepository.findArticleById(articleId);
        article.setReject(true);
        articleRepository.save(article);
        return Response.successRejectArticle();
    }
    public Response deleteArticle(int articleId) {
        articleRepository.deleteArticleById(articleId);
        return Response.successDeleteArticle();
    }
}
