package com.back.controller.themeController;

import com.back.dto.ThemeAndArticleCount;
import com.back.get.ThemesList;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("theme")
@CrossOrigin(origins = "*")
public class GetAllThemeAndArticleCountController {
    @Resource
    private ThemesList themesList;
    @Description("获取所有分类和对应的文章数量")
    @GetMapping("/getAllThemeAndArticleCount")
    public List<ThemeAndArticleCount> getAllThemeAndArticleCount(@RequestParam int userId) {
        return themesList.getThemesAndArticleCountList(userId);
    }
}
