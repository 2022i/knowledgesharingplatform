package com.back.controller;

import com.back.dto.RenderedTheme;
import com.back.get.preparation.ThemeRenderingPreparation;
import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/search")
public class ThemeSearchController {
    @Resource
    private ThemeRepository themeRepository;
    @Resource
    private ThemeRenderingPreparation themeRenderingPreparation;
    @GetMapping ("/theme")
    public List<RenderedTheme> searchTheme(@RequestParam String name, @RequestParam int userId) {
        return themeRenderingPreparation.getRenderedThemes(themeRepository.findThemesByName(name),userId);
    }
}
