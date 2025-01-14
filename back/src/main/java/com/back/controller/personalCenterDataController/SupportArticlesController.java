package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.SupportArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.get.preparation.RenderedArticleBooleanSet;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
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
    @Resource
    private RenderedArticleBooleanSet renderedArticleBooleanSet;
    @Description("个人中心接口，获取用户点赞的文章详细信息列表，返回文章数据用于渲染")
    @GetMapping("/getSupportArticles")
    public List<RenderedArticle> getSupportArticles(@RequestParam int userId) {
        return renderedArticleBooleanSet.setRenderedArticleBoolean(articleRenderingPreparation.getRenderedArticles(supportArticlesList.getArticlesList(userId)),userId);
    }
}
