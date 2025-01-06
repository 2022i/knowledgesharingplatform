package com.back.controller.AuditorController;

import com.back.auditor.ArticleCheck;
import com.back.dto.UnCheckArticle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class GetUnCheckArticlesList {
    @Resource
    private ArticleCheck articleCheck;
    @GetMapping("/getUnCheckArticles")
    public List<UnCheckArticle> getUnCheckArticles() {
        return articleCheck.getUnCheckArticles();
    }
}
