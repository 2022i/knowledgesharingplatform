package com.back.userAction;

import com.back.dto.Response;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UndoSubmitDeleteRequest {
    @Resource
    private ArticleRepository articleRepository;
    public Response undoSubmitDeleteRequest(int articleId) {
        Article article=articleRepository.findArticleById(articleId);
        article.setDelete(false);
        articleRepository.save(article);
        return Response.successUndoDelete();
    }
}
