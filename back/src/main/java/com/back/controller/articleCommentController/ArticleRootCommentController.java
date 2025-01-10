package com.back.controller.articleCommentController;

import com.back.dto.RenderedComment;
import com.back.get.CommentsList.ArticleRootCommentsList;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleComment")
public class ArticleRootCommentController {
    @Resource
    private ArticleRootCommentsList articleRootCommentsList;
    @Description("当读者点击文章的“评论”按钮时调用改接口，获取文章的根评论")
    @GetMapping("/getArticleRootComments")
    public List<RenderedComment> getRootComments(@RequestParam int articleId) {
        return articleRootCommentsList.getArticleRootCommentsList(articleId);
    }
}
