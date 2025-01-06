package com.back.controller.articleCommentController;

import com.back.dto.RenderedComment;
import com.back.get.CommentsList.ArticleRootCommentsList;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleComment")
public class ArticleRootCommentController {
    @Resource
    private ArticleRootCommentsList articleRootCommentsList;
    @GetMapping("/getArticleRootComments")
    public List<RenderedComment> getRootComments(@RequestParam int articleId) {
        return articleRootCommentsList.getArticleRootCommentsList(articleId);
    }
}
