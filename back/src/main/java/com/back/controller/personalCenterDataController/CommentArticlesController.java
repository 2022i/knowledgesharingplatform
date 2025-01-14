package com.back.controller.personalCenterDataController;

import com.back.dto.article.ArticleAndComment;
import com.back.get.ArtilcesList.CommentArticlesList;
import com.back.get.preparation.RenderedArticleBooleanSet;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class CommentArticlesController {
    @Resource
    private CommentArticlesList commentArticlesList;
    @Resource
    private RenderedArticleBooleanSet renderedArticleBooleanSet;
    @Description("个人中心接口，获取用户评论过的文章详细信息列表和用户的评论的内容，返回这些数据用于渲染")
    @GetMapping("/getCommentArticles")
    public List<ArticleAndComment> getCommentArticles(@RequestParam int userId) {
        return commentArticlesList.getCommentAndArticleList(userId);
    }
}
