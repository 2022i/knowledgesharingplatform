package com.back.controller.personalCenterDataController.ArticleController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.PublishingArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class PublishingArticlesController {
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Resource
    private PublishingArticlesList publishingArticlesList;
    @Description("个人中心接口,获取用户发布待审核的文章")
    @GetMapping("/getPublishingArticles")
    public List<RenderedArticle> getPublishingArticlesList(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(publishingArticlesList.getPublishingArticlesList(userId));
    }
}
