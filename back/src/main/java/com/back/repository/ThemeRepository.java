package com.back.repository;

import com.back.index.Theme;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThemeRepository extends ElasticsearchRepository<Theme,Integer > {
    Theme findThemeById(int id);
    @NotNull List<Theme> findAll();
    List<Theme> findThemesByName(String name);
}
