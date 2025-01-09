package com.back.controller.HomePageArticleController;

import com.back.get.HomePageArticle.FollowArticle;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class FollowArticlesController {
    @Resource
    private FollowArticle followArticle;

    @GetMapping("/followArticles")
    public List<Article>   getFollowArticles(@RequestParam int userId){
        return followArticle.getFollowArticles(userId);
    }
}
