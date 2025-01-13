package com.back.controller.personalCenterDataController.ArticleController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.PublishedArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class PublishedArticlesController {
    @Resource
    private PublishedArticlesList publishedArticleList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Description("个人中心接口，获取用户已发布的文章")
    @GetMapping("/getPublishedArticles")
    public List<RenderedArticle> getPublishedArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(publishedArticleList.getPublishedArticlesList(userId));
    }
}
