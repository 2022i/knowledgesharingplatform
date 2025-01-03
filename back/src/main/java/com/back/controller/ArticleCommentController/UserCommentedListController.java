package com.back.controller.ArticleCommentController;

import com.back.dto.RenderingComment;
import com.back.get.CommentsList.UserCommentedList;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleComment")
public class UserCommentedListController {
    @Resource
    private UserCommentedList userCommentedList;
    @GetMapping("/getUserCommentedList")
    public List<RenderingComment> getUserCommentedList(@RequestParam int commentId, @RequestParam int articleId) {
        return userCommentedList.getUserCommentedList(commentId, articleId);
    }
}
