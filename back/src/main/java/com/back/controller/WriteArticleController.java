package com.back.controller;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.dto.article.ArticleFromFront;
import com.back.dto.Response;
import com.back.write.WriteArticle;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteArticleController {
    @Resource
    private WriteArticle writeArticle;
    @Description("发布文章，点击最后一个AI流程的确定键时调用这个接口，用于将文章存入数据库")
    @PostMapping("/article")
    public Response writeArticle(@RequestBody ArticleFromFront articleFromFront) throws NoApiKeyException, InputRequiredException {
        return writeArticle.writeArticle(articleFromFront);
    }
}
