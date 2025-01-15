package com.back.get.preparation;

import com.back.dto.article.RenderedArticle;
import com.back.dto.article.RenderedViewArticle;
import com.back.get.ArtilcesList.ViewArticlesList;
import com.back.index.Article;
import com.back.repository.ViewDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ViewArticlePreparation {
    @Resource
    private ViewArticlesList viewArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Resource
    private ViewDataRepository viewDataRepository;
    public List<RenderedViewArticle> getRenderedViewArticles(int userId){
        List<Article> viewArticles = viewArticlesList.getArticlesList(userId);
        List<RenderedArticle> renderedArticles = articleRenderingPreparation.getRenderedArticles(viewArticles, userId);
        List<RenderedViewArticle> renderedViewArticles = new ArrayList<>();
        for (RenderedArticle renderedArticle : renderedArticles) {
            RenderedViewArticle renderedViewArticle=new RenderedViewArticle();
            renderedViewArticle.setArticle(renderedArticle);
            renderedViewArticle.setViewTime(getViewTime(renderedArticle.getId()));
            renderedViewArticles.add(renderedViewArticle);
        }
        return renderedViewArticles;
    }
    private LocalDateTime getViewTime(int articleId){
        return viewDataRepository.findViewDataByArticleId(articleId).getViewTime();
    }
}
