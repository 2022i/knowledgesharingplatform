package com.back.controller.auditorController;

import com.back.auditor.DeletingArticleGet;
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
public class GetDeletingArticlesListController {
    @Resource
    private DeletingArticleGet deletingArticleGet;
    @Description("获取待删除文章列表，这个接口仅仅返回文章的概要信息的列表，审核员点击“指定文章”调用http://localhost:8081/server/auditor/getAuditorArticle?得到文章的详细信息用于阅读")
    @GetMapping("/getDeletingArticlesList")
    public List<AuditorArticle> getDeletingArticlesList() {
        return deletingArticleGet.getDeletingArticles();
    }
}
