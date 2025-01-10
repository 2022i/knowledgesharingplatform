package com.back.controller.articleCommentController;

import com.back.dto.RenderedComment;
import com.back.get.RenderedCommentsListGeneration;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleComment")
public class ArticleAllCommentController {
    @Resource
    private RenderedCommentsListGeneration renderedCommentsListGeneration;
    @Description("获取文章的所有评论，视情况使用")
    @GetMapping("/getArticleAllComments")
    public List<RenderedComment> getComments(@RequestParam int articleId) {
        return renderedCommentsListGeneration.getArticleRenderingCommentList(articleId);
    }
}
