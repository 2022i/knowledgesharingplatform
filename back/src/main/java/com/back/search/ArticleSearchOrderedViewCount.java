package com.back.search;

import com.back.dto.RenderedArticle;
import com.back.index.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSearchOrderedViewCount extends ArticleSearch{
    @Override
    public List<RenderedArticle> getArticles(String keyword){
        List<Article> articles=articleRepository.findArticlesByTitleOrContent(keyword,keyword);
        List<RenderedArticle> renderedArticles = articleRenderingPreparation.getRenderedArticles(articles);
        renderedArticles.sort((a1, a2) -> a2.getViewUserCount() - a1.getViewUserCount());
        return renderedArticles;
    }
}
