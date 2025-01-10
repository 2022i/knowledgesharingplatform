package com.back.controller;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("")
public class UserDeleteArticleController {
    @Resource
    private ArticleRepository articleRepository;
    //待重构
    @PutMapping ("/userDeleteArticle")
    public void deleteArticle(int articleId){
        Article article=articleRepository.findArticleById(articleId);
        article.setDelete(true);
        articleRepository.save(article);
    }
}
