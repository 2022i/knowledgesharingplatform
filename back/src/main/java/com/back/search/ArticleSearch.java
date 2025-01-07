package com.back.search;

import com.back.dto.Article.RenderedArticle;
import com.back.get.ArticleRenderingPreparation;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public abstract class ArticleSearch {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected ArticleRenderingPreparation articleRenderingPreparation;
    abstract public List<RenderedArticle> getArticles(String keyword);
}
