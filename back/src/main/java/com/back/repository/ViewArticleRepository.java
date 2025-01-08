package com.back.repository;

import com.back.index.ViewArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ViewArticleRepository extends ElasticsearchRepository<ViewArticle,Integer> {
    ViewArticle findViewArticleByArticleId(int articleId);
    void deleteViewArticleByArticleId(int articleId);
}
