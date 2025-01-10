package com.back.controller.articleCommentController;

import com.back.dto.RenderedComment;
import com.back.get.CommentsList.UserCommentedList;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/articleComment")
public class UserCommentedListController {
    @Resource
    private UserCommentedList userCommentedList;
    @Description("当读者点击其他用户对文章的评论的“评论按钮”时，调用该接口，返回该对文章的评论的所有“评论”")
    @GetMapping("/getUserCommentedList")
    public List<RenderedComment> getUserCommentedList(@RequestParam int commentId) {
        return userCommentedList.getUserCommentedList(commentId);
    }
}
