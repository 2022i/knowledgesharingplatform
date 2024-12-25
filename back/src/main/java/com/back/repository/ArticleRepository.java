package com.back.repository;

import com.back.index.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findArticleByTitle(String title);
    List<Article> findArticleByContent(String content);
}
