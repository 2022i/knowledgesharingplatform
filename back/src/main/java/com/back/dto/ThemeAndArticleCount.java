package com.back.dto;

import com.back.index.Theme;
import lombok.Data;

@Data
public class ThemeAndArticleCount {
    private Theme theme;
    private int articleCount;
}
