package com.back.controller.articleCommentController;

import com.back.dto.RenderedComment;
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
    public List<RenderedComment> getUserCommentedList(@RequestParam int commentId) {
        return userCommentedList.getUserCommentedList(commentId);
    }
}
