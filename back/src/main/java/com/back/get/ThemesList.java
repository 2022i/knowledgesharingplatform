package com.back.get;

import com.back.index.Theme;
import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemesList {
    @Resource
    private ThemeRepository themeRepository;
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
