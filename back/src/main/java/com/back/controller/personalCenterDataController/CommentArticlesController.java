package com.back.controller.personalCenterDataController;

import com.back.dto.RenderedArticle;
import com.back.get.ArticleRenderingPreparation;
import com.back.get.ArtilcesList.CommentArticlesList;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class CommentArticlesController {
    @Resource
    private CommentArticlesList commentArticlesList;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @GetMapping("/getCommentArticles")
    public List<RenderedArticle> getCommentArticles(int userId) {
        return articleRenderingPreparation.getRenderedArticles(commentArticlesList.getArticlesList(userId));
    }
}
