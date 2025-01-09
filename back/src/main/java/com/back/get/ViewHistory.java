package com.back.get;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.ViewArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViewHistory {
    @Resource
    private ViewArticlesList viewArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    public List<RenderedArticle> getViewArticle(int userId){
        List<Article> viewArticles = viewArticlesList.getArticlesList(userId);
        return articleRenderingPreparation.getRenderedArticles(viewArticles);
    }
}
