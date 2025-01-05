package com.back.repository;

import com.back.index.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {
    Article findArticleById(int id);
    List<Article> findArticleByThemeId(int themeId);
}
