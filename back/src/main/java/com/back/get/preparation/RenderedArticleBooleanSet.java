package com.back.get.preparation;

import com.back.dto.article.RenderedArticle;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RenderedArticleBooleanSet {
    @Resource
    private ArticleRepository articleRepository;
    public RenderedArticle setRenderedArticleBoolean(RenderedArticle renderedArticle, int userId) {
        renderedArticle.setCollect(isCollected(renderedArticle, userId));
        renderedArticle.setSupport(isSupported(renderedArticle, userId));
        renderedArticle.setOppose(isOpposed(renderedArticle, userId));
        return renderedArticle;
    }
    public List<RenderedArticle> setRenderedArticleBoolean(List<RenderedArticle> renderedArticles, int userId) {
        List<RenderedArticle> result=new ArrayList<>();
        for (RenderedArticle renderedArticle : renderedArticles) {
            result.add(setRenderedArticleBoolean(renderedArticle, userId));
        }
        return result;
    }
    private boolean isOpposed(RenderedArticle renderedArticle, int userId) {
        return articleRepository.findArticleById(renderedArticle.getId()).getOpposeUserIds().contains(userId);
    }
    private boolean isSupported(RenderedArticle renderedArticle, int userId) {
        return articleRepository.findArticleById(renderedArticle.getId()).getSupportUserIds().contains(userId);
    }
    private boolean isCollected(RenderedArticle renderedArticle, int userId) {
        return articleRepository.findArticleById(renderedArticle.getId()).getCollectUserIds().contains(userId);
    }
}
