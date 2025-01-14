package com.back.controller.homePageArticleController;

import com.back.dto.article.RenderedArticle;
import com.back.get.HomePageArticle.FollowArticle;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.get.preparation.RenderedArticleBooleanSet;
import com.back.index.Article;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class FollowArticlesController {
    @Resource
    private FollowArticle followArticle;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Resource
    private RenderedArticleBooleanSet renderedArticleBooleanSet;
    @GetMapping("/followArticles")
    @Description("获取用户关注的文章，包括关注的人的文章以及关注的主题文章")
    public List<RenderedArticle> getFollowArticles(@RequestParam int userId){
        List<Article> articles = followArticle.getRecommendArticles(userId);
        return renderedArticleBooleanSet.setRenderedArticleBoolean(articleRenderingPreparation.getRenderedArticles(articles),userId);
    }
}
