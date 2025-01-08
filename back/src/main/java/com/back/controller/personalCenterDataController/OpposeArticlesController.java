package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.OpposeArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class OpposeArticlesController {
    @Resource
    private OpposeArticlesList opposeArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/getOpposeArticles")
    public List<RenderedArticle> getOpposeArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(opposeArticlesList.getArticlesList(userId));
    }
}
