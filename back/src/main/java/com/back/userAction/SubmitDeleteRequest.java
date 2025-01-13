package com.back.userAction;

import com.back.dto.Response;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SubmitDeleteRequest {
    @Resource
    private ArticleRepository articleRepository;
    public Response submitDeleteRequest(int articleId) {
        Article article=articleRepository.findArticleById(articleId);
        article.setDelete(true);
        articleRepository.save(article);
        return Response.successApplyDelete();
    }
}
