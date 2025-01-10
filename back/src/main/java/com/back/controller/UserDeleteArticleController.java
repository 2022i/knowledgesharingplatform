package com.back.controller;

import com.back.dto.Response;
import com.back.userAction.ArticleDeletion;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("")
public class UserDeleteArticleController {
    @Resource
    private ArticleDeletion articleDeletion;
    //待重构
    @PutMapping ("/userDeleteArticle")
    public Response deleteArticle(@RequestParam int articleId){
        return articleDeletion.deleteArticle(articleId);
    }
}
