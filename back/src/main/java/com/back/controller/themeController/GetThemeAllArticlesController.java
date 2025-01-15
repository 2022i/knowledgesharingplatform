package com.back.controller.themeController;

import com.back.dto.article.RenderedArticle;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.get.preparation.RenderedArticleBooleanSet;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("theme")
@CrossOrigin(origins = "*")
public class GetThemeAllArticlesController {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Resource
    private RenderedArticleBooleanSet renderedArticleBooleanSet;
    @GetMapping("/getThemeAllArticles")
    public List<RenderedArticle> getThemeAllArticles(@RequestParam int themeId, @RequestParam int userId) {
        return renderedArticleBooleanSet.setRenderedArticleBoolean(articleRenderingPreparation.getRenderedArticles(articleRepository.findArticlesByThemeId(themeId)),userId);
    }
}
