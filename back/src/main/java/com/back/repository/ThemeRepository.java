package com.back.repository;

import com.back.index.Theme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends ElasticsearchRepository<Theme,Integer > {
    Theme findThemeById(int id);
}
