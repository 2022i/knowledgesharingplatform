package com.back.get;

import com.back.dto.ThemeAndArticleCount;
import com.back.get.ArtilcesList.ThemeArticlesList;
import com.back.get.preparation.ThemeRenderingPreparation;
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
    @Resource
    private ThemeRenderingPreparation themeRenderingPreparation;
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
    public List<ThemeAndArticleCount> getThemesAndArticleCountList(int userId) {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeAndArticleCount> themeAndArticleCounts = new ArrayList<>();
        for(Theme theme: themes) {
            themeAndArticleCounts.add(getThemeAndArticleCount(theme,userId));
        }
        return themeAndArticleCounts;
    }
    private ThemeAndArticleCount getThemeAndArticleCount(Theme theme,int userId) {
        ThemeAndArticleCount themeAndArticleCount = new ThemeAndArticleCount();
        themeAndArticleCount.setTheme(themeRenderingPreparation.getRenderedTheme(theme,userId));
        themeAndArticleCount.setArticleCount(themeArticlesList.getThemeArticles(theme.getId()).size());
        return themeAndArticleCount;
    }
}
