package com.back.search;

import com.back.index.Article;
import com.back.dto.article.RenderedArticle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSearchOrderedSupportCount extends ArticleSearch{
    @Override
    public List<RenderedArticle> getArticles(String keyword){
        List<Article> articles=articleRepository.findArticlesByTitleOrContent(keyword,keyword);
        List<RenderedArticle> renderedArticles = articleRenderingPreparation.getRenderedArticles(articles);
        renderedArticles.sort((a1, a2) -> a2.getSupportUserCount() - a1.getSupportUserCount());
        return renderedArticles;
    }
}
