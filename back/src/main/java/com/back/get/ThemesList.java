package com.back.get;

import com.back.dto.ThemeAndArticleCount;
import com.back.get.ArtilcesList.ThemeArticlesList;
import com.back.index.Theme;
import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThemesList {
    @Resource
    private ThemeRepository themeRepository;
    @Resource
    private ThemeArticlesList themeArticlesList;
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
    public List<ThemeAndArticleCount> getThemesAndArticleCountList() {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeAndArticleCount> themeAndArticleCounts = new ArrayList<>();
        for(Theme theme: themes) {
            themeAndArticleCounts.add(getThemeAndArticleCount(theme));
        }
        return themeAndArticleCounts;
    }
    private ThemeAndArticleCount getThemeAndArticleCount(Theme theme) {
        ThemeAndArticleCount themeAndArticleCount = new ThemeAndArticleCount();
        themeAndArticleCount.setTheme(theme);
        themeAndArticleCount.setArticleCount(themeArticlesList.getThemeArticles(theme.getId()).size());
        return themeAndArticleCount;
    }
}
