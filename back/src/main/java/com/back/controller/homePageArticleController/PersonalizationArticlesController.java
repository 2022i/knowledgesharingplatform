package com.back.controller.homePageArticleController;

import com.back.dto.Response;
import com.back.dto.article.RenderedArticle;
import com.back.get.HomePageArticle.PersonalizationArticleForUser;
import com.back.get.HomePageArticle.PersonalizationArticleForVisitor;
import com.back.get.HomePageArticle.PersonalizationArticleForVisitor;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.index.Article;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class PersonalizationArticlesController {
    @Resource
    private PersonalizationArticleForVisitor personalizationArticleforVisitor;
    @Resource
    private PersonalizationArticleForUser personalizationArticleforUser;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;

    @GetMapping("/barrierArticlesVisitor")
    @Description("游客身份下推荐信息壁垒文章部分")
    public List<RenderedArticle> getBarrierArticles(@RequestParam List<Integer> ids){
        List<Article> articles=personalizationArticleforVisitor.getBarrierArticles(ids);
        return articleRenderingPreparation.getRenderedArticles(articles);
    }

    @GetMapping("/recommendedArticlesVisitor")
    @Description("游客身份下获取推荐文章")
    public List<RenderedArticle> getRecommendedArticles(@RequestParam List<Integer> ids) {
        List<Article> articles=personalizationArticleforVisitor.getRecommendedArticles(ids);
        return articleRenderingPreparation.getRenderedArticles(articles);
    }

    @GetMapping("/barrierArticlesUser")
    @Description("用户身份下获取信息壁垒文章")
    public List<RenderedArticle> getBarrierArticles(@RequestParam int userId) {
        List<Article> articles=personalizationArticleforUser.getBarrierArticles(userId);
        return articleRenderingPreparation.getRenderedArticles(articles);
    }

    @GetMapping("/recommendedArticlesUser")
    @Description("用户身份下获取推荐文章")
    public List<RenderedArticle> getRecommendedArticles(@RequestParam int userId) {
        List<Article> articles=personalizationArticleforUser.getRecommendedArticles(userId);
        return articleRenderingPreparation.getRenderedArticles(articles);
    }
}