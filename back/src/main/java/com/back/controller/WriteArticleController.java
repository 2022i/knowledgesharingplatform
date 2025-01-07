package com.back.controller;

import com.back.dto.Article.ArticleFromFront;
import com.back.dto.Response;
import com.back.write.WriteArticle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteArticleController {
    @Resource
    private WriteArticle writeArticle;
    @PostMapping("/article")
    public Response writeArticle(ArticleFromFront articleFromFront) {
        return writeArticle.writeArticle(articleFromFront);
    }
}
