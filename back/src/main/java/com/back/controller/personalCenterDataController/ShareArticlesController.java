package com.back.controller.personalCenterDataController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.ShareArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
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
    @Description("个人中心接口，获取用户分享的文章详细信息列表，返回文章数据用于渲染")
    @GetMapping("/getShareArticles")
    public List<RenderedArticle> getShareArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(shareArticlesList.getArticlesList(userId));
    }
}
