package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.ShareArticlesList;
import com.back.get.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class ShareArticlesController {
    @Resource
    private ShareArticlesList shareArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/getShareArticles")
    public List<RenderedArticle> getShareArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(shareArticlesList.getArticlesList(userId));
    }
}
