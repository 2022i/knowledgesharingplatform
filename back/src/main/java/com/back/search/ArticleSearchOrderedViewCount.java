package com.back.search;

import com.back.dto.article.RenderedArticle;
import com.back.index.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSearchOrderedViewCount extends ArticleSearch{
    @Override
    public List<RenderedArticle> getArticles(String keyword){
        List<Article> articles=articleRepository.findArticlesByTitleOrContent(keyword,keyword);
        articles.removeIf(article -> !article.isCheck() || article.isDelete() || article.isReject());
        List<RenderedArticle> renderedArticles = articleRenderingPreparation.getRenderedArticles(articles);
        renderedArticles.sort((a1, a2) -> a2.getViewUserCount() - a1.getViewUserCount());
        return renderedArticles;
    }
}
