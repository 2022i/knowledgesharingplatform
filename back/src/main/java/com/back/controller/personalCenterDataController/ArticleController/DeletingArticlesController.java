package com.back.controller.personalCenterDataController.ArticleController;

import com.back.dto.article.RenderedArticle;
import com.back.get.ArtilcesList.DeletingArticlesList;
import com.back.get.preparation.ArticleRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class DeletingArticlesController {
    @Resource
    private DeletingArticlesList deletingArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Description("个人中心接口，获取用户提交删除请求，待审核的文章")
    @GetMapping("/getDeletingArticles")
    public List<RenderedArticle> getDeletingArticles(@RequestParam int userId) {
        return articleRenderingPreparation.getRenderedArticles(deletingArticlesList.getDeletingArticlesList(userId), userId);
    }
}
