package com.back.controller.homePageArticleController;


import com.back.get.HomePageArticle.HotArticle;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class HotArticlesController {
    @Resource
    private HotArticle hotArticle;

    @GetMapping("/hotArticles")
    public List<Article> getHotArticles(){
        return hotArticle.getHotArticles();
    }
}
