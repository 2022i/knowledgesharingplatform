package com.back.repository;

import com.back.index.ViewData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewDataRepository extends ElasticsearchRepository<ViewData,Integer> {
    ViewData findViewDataByArticleId(int articleId);
    void deleteViewArticleByArticleId(int articleId);
    ViewData findViewDataById(int id);
}
