package com.back.controller;

import com.back.dto.article.RenderedArticle;
import com.back.get.RenderedArticleGet;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class GetArticleController {
    @Resource
    private RenderedArticleGet renderedArticleGet;
    @Description("获取前端渲染需要的文章数据")
    @GetMapping ("/getRenderedArticle")
    public RenderedArticle getRenderedArticle(@RequestParam int articleId) {
        return renderedArticleGet.getRenderedArticle(articleId);
    }
}
