package com.back.get.CommentsList;

import com.back.dto.RenderingComment;
import com.back.get.RenderingCommentsListGeneration;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentedList {
    @Resource
    private RenderingCommentsListGeneration renderingCommentsListGeneration;

    public List<RenderingComment> getUserCommentedList(int commentId) {
            return renderingCommentsListGeneration.getUserRenderingCommentedList(commentId);
    }
}
