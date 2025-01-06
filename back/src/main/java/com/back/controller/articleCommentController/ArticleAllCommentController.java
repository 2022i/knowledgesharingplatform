package com.back.controller.articleCommentController;

import com.back.dto.RenderingComment;
import com.back.get.RenderingCommentsListGeneration;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleComment")
public class ArticleAllCommentController {
    @Resource
    private RenderingCommentsListGeneration renderingCommentsListGeneration;
    @GetMapping("/getArticleAllComments")
    public List<RenderingComment> getComments(@RequestParam int articleId) {
        return renderingCommentsListGeneration.getArticleRenderingCommentList(articleId);
    }
}
