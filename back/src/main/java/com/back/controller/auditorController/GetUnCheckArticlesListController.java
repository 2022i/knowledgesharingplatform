package com.back.controller.auditorController;

import com.back.auditor.UnCheckArticleGet;
import com.back.dto.article.AuditorArticle;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
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
    @Description("获取待审核文章列表，审核员点击“指定文章”调用http://localhost:8081/server/auditor/getAuditorArticle?得到文章的详细信息用于阅读")
    @GetMapping("/getUnCheckArticles")
    public List<AuditorArticle> getUnCheckArticles() {
        return unCheckArticleGet.getUnCheckArticles();
    }
}
