package com.back.controller.personalCenterDataController.ArticleController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.UnpublishedArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class UnPublishedArticlesController {
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Resource
    private UnpublishedArticlesList unpublishedArticlesList;
    @GetMapping("/getUnpublishedArticles")
    public List<RenderedArticle> getUnpublishedArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(unpublishedArticlesList.getUnpublishedArticlesList(userId),userId );
    }
}
