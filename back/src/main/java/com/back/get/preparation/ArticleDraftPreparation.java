package com.back.get.preparation;

import com.back.dto.article.ArticleDraft;
import com.back.index.Article;
import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleDraftPreparation {
    @Resource
    private ThemeRepository themeRepository;
    public List<ArticleDraft> getArticleDrafts(List<Article> articles) {
        List<ArticleDraft> articleDrafts = new ArrayList<>();
        for (Article article : articles) {
            articleDrafts.add(getArticleDraft(article));
        }
        return articleDrafts;
    }
    private ArticleDraft getArticleDraft(Article article) {
        ArticleDraft articleDraft = new ArticleDraft();
        articleDraft.setId(article.getId());
        articleDraft.setTitle(article.getTitle());
        articleDraft.setContent(article.getContent());
        articleDraft.setTheme(themeRepository.findThemeById(article.getThemeId()));
        return articleDraft;
    }
}
