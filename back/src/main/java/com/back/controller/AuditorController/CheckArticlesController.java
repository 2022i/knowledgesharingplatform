package com.back.controller.AuditorController;

import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class CheckArticlesController {
    @Resource
    private ArticleCheck articleCheck;
    @PutMapping("/approveArticle")
    public Response checkArticles(int articleId) {
        return articleCheck.approveArticle(articleId);
    }
    @PutMapping("/rejectArticle")
    public Response rejectArticles(int articleId) {
        return articleCheck.rejectArticle(articleId);
    }
}
