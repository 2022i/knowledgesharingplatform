package com.back.controller.HomePageArticleController;


import com.back.get.HomePageArticle.HotArticle;
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

    @GetMapping("/hotArticles")
    @Description("获取热榜文章，以文章浏览量排名")
    public List<Article> getHotArticles(){
        return hotArticle.getHotArticles();
    }
}
