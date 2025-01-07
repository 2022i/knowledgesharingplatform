package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.CollectArticlesList;
import com.back.get.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class CollectArticlesController {
    @Resource
    private CollectArticlesList collectArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/getCollectArticles")
    public List<RenderedArticle> getCollectArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(collectArticlesList.getArticlesList(userId));
    }
}
