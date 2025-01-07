package com.back.controller.personalCenterDataController;

import com.back.dto.article.ArticleAndComment;
import com.back.get.ArtilcesList.CommentArticlesList;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class CommentArticlesController {
    @Resource
    private CommentArticlesList commentArticlesList;
    @GetMapping("/getCommentArticles")
    public List<ArticleAndComment> getCommentArticles(@RequestParam int userId) {
        return commentArticlesList.getCommentAndArticleList(userId);
    }
}
