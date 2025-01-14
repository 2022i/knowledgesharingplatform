package com.back.dto;

import lombok.Data;

@Data
public class ThemeAndArticleCount {
    private RenderedTheme theme;
    private int articleCount;
}
