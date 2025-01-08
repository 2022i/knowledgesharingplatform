package com.back.controller.auditorController;

import com.back.auditor.UnCheckArticleGet;
import com.back.dto.article.AuditorArticle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class GetUncheckArticleController {
    @Resource
    private UnCheckArticleGet unCheckArticleGet;
    @GetMapping("/getUnCheckArticle")
    public AuditorArticle getUncheckArticle(@RequestParam int articleId) {
        return unCheckArticleGet.getUncheckArticle(articleId);
    }
}
