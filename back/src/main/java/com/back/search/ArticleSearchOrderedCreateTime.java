package com.back.search;

import com.back.dto.RenderedArticle;
import com.back.index.Article;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleSearchOrderedCreateTime extends ArticleSearch{
    @Override
    public List<RenderedArticle> getArticles(String keyword){
        List<Article> articles=articleRepository.findArticlesByTitleOrContent(keyword,keyword);
        articles.sort((a1, a2) -> a2.getCreateTime().compareTo(a1.getCreateTime()));
        return articleRenderingPreparation.getRenderedArticles(articles);
    }
}
