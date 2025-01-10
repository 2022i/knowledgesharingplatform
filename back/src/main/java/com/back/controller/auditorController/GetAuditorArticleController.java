package com.back.controller.auditorController;

import com.back.auditor.UnCheckArticleGet;
import com.back.dto.article.AuditorArticle;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class GetAuditorArticleController {
    @Resource
    private UnCheckArticleGet unCheckArticleGet;
    @Description("获取待审核文章,注意仅返回一个用于审核员阅读的文章的相关信息")
    @GetMapping("/getAuditorArticle")
    public AuditorArticle getUncheckArticle(@RequestParam int articleId) {
        return unCheckArticleGet.getUncheckArticle(articleId);
    }
}
