package com.back.controller.AuditorController;

import com.back.auditor.UnCheckArticleGet;
import com.back.dto.article.AuditorArticle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class GetUnCheckArticlesListController {
    @Resource
    private UnCheckArticleGet unCheckArticleGet;
    @GetMapping("/getUnCheckArticles")
    public List<AuditorArticle> getUnCheckArticles() {
        return unCheckArticleGet.getUnCheckArticles();
    }
}
