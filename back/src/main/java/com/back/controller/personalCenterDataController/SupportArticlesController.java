package com.back.controller.personalCenterDataController;

import com.back.dto.RenderedArticle;
import com.back.get.ArtilcesList.SupportArticlesList;
import com.back.get.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class SupportArticlesController {
    @Resource
    private SupportArticlesList supportArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/getSupportArticles")
    public List<RenderedArticle> getSupportArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(supportArticlesList.getArticlesList(userId));
    }
}
