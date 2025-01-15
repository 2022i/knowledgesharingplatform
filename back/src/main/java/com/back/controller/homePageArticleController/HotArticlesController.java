package com.back.controller.homePageArticleController;


import com.back.dto.article.RenderedArticle;
import com.back.get.HomePageArticle.HotArticle;
import com.back.get.preparation.ArticleRenderingPreparation;
import com.back.index.Article;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class HotArticlesController {
    @Resource
    private HotArticle hotArticle;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/hotArticles")
    @Description("获取热门文章")
    public List<RenderedArticle> getHotArticles(@RequestParam int userId){
        List<Article> articles = hotArticle.getHotArticles();
        return articleRenderingPreparation.getRenderedArticles(articles,userId);
    }
}

