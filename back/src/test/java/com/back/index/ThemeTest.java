package com.back.index;

import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ThemeTest {
    @Resource
    private ThemeRepository themeRepository;
    @Test
    void initializeTheme() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Theme theme=new Theme();
            theme.setId(i);
            theme.setName("theme"+i);
            theme.setFatherId(i);
            themes.add(theme);
        }
        themeRepository.saveAll(themes);
    }
}