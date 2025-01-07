package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.WriteArticlesList;
import com.back.get.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class WriteArticlesController {
    @Resource
    private WriteArticlesList writeArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/getWriteArticles")
    public List<RenderedArticle> getWriteArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(writeArticlesList.getArticlesList(userId));
    }
}
