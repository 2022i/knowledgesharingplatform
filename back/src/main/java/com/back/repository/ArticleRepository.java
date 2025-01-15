package com.back.repository;

import com.back.index.Article;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, Integer> {
    Article findArticleById(int id);
    List<Article> findArticleByThemeId(int themeId);
    List<Article> findArticlesByTitleOrContentAndCheck(String title, String content, boolean check);
    List<Article> findArticlesByCheck(boolean check);
    void deleteArticleById(int id);
    List<Article> findArticlesByDelete(boolean delete);
    @NotNull List<Article> findAll();
    List<Article> findArticlesByThemeIdAndCheckAndRejectAndDelete(int themeId, boolean check, boolean reject, boolean delete);
    List<Article> findArticlesByAuthorIdAndDraft(int authorId, boolean draft);
    List<Article> findArticlesByCheckAndDeleteAndReject(boolean check, boolean delete, boolean reject);
    List<Article> findArticlesByAuthorIdAndCheckAndRejectAndDelete(int authorId, boolean check, boolean reject, boolean delete);
}
