package com.back.controller.themeController;

import com.back.get.ThemesList;
import com.back.index.Theme;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("theme")
@CrossOrigin(origins = "*")
public class GetAllThemesController {
    @Resource
    private ThemesList themesList;
    @Description("用户在写文章选择一个分类前，调用此接口获取所有分类")
    @GetMapping("/getAllThemes")
    public List<Theme> getAllThemes() {
        return themesList.getAllThemes();
    }
}
