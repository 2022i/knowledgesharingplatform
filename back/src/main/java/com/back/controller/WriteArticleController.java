package com.back.controller;

import com.back.dto.article.ArticleFromFront;
import com.back.dto.Response;
import com.back.write.WriteArticle;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
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
    @Description("发布文章，点击最后一个AI流程的确定键时调用这个接口，用于将文章存入数据库")
    @PostMapping("/article")
    public Response writeArticle(ArticleFromFront articleFromFront) {
        return writeArticle.writeArticle(articleFromFront);
    }
}
