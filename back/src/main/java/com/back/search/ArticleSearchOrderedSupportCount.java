package com.back.search;

import com.back.index.Article;
import com.back.dto.article.RenderedArticle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSearchOrderedSupportCount extends ArticleSearch{
    @Override
    public List<RenderedArticle> getArticles(String keyword, int userId){
        List<Article> articles=articleRepository.findArticlesByTitleOrContentAndCheck(keyword,keyword, true);
        articles.removeIf(article -> !article.isCheck() || article.isDelete() || article.isReject());
        List<RenderedArticle> renderedArticles = articleRenderingPreparation.getRenderedArticles(articles, userId);
        renderedArticles.sort((a1, a2) -> a2.getSupportUserCount() - a1.getSupportUserCount());
        return renderedArticles;
    }
}
