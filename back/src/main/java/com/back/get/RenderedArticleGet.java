package com.back.get;

import com.back.dto.article.RenderedArticle;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RenderedArticleGet {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    public RenderedArticle getRenderedArticle(int articleId, int userId) {
         return articleRenderingPreparation.getRenderedArticle(articleRepository.findArticleById(articleId),userId);
    }
}
