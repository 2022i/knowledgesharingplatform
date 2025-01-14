package com.back.get.preparation;

import com.back.dto.RenderedTheme;
import com.back.index.Theme;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThemeRenderingPreparation {
    @Resource
    private UserDataRepository userDataRepository;
    public List<RenderedTheme> getRenderedThemes(List<Theme> themes, int userId){
        List<RenderedTheme> renderedThemes = new ArrayList<>();
        for (Theme theme : themes) {
            renderedThemes.add(getRenderedTheme(theme,userId));
        }
        return renderedThemes;
    }
    public RenderedTheme getRenderedTheme(Theme theme,int userId) {
        RenderedTheme renderedTheme = new RenderedTheme();
        renderedTheme.setId(theme.getId());
        renderedTheme.setName(theme.getName());
        renderedTheme.setSubscribed(isSubscribed(theme,userId));
        return renderedTheme;
    }
    private boolean isSubscribed(Theme theme,int userId){
        return userDataRepository.findUserDataById(userId).getSubscriptionThemeId().contains(theme.getId());
    }
}


