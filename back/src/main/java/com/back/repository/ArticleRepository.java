package com.back.repository;

import com.back.index.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {
    Article findArticleById(int id);
    List<Article> findArticleByThemeId(int themeId);
    List<Article> findArticlesByTitleOrContent(String title, String content);
    List<Article> findArticlesByCheck(boolean check);
    void deleteArticleById(int id);
}
