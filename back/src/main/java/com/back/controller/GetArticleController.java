package com.back.controller;

import com.back.dto.article.RenderedArticle;
import com.back.get.RenderedArticleGet;
import com.back.get.preparation.RenderedArticleBooleanSet;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class GetArticleController {
    @Resource
    private RenderedArticleGet renderedArticleGet;
    @Resource
    private RenderedArticleBooleanSet renderedArticleBooleanSet;
    @Description("获取前端渲染需要的文章数据")
    @GetMapping ("/getRenderedArticle")
    public RenderedArticle getRenderedArticle(@RequestParam int articleId,@RequestParam int userId){
        return renderedArticleBooleanSet.setRenderedArticleBoolean(renderedArticleGet.getRenderedArticle(articleId),userId);
    }
}
